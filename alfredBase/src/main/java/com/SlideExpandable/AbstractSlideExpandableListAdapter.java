package com.SlideExpandable;

import java.util.BitSet;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Wraps a ListAdapter to give it expandable list view functionality. The main
 * thing it does is add a listener to the getToggleButton which expands the
 * getExpandableView for each list item.
 * 
 * @author tjerk
 * @date 6/9/12 4:41 PM
 */
public abstract class AbstractSlideExpandableListAdapter extends BaseAdapter {
	/**
	 * Reference to the last expanded list item. Since lists are recycled this
	 * might be null if though there is an expanded list item
	 */
	private View lastOpen = null;
	/**
	 * The position of the last expanded list item. If -1 there is no list item
	 * expanded. Otherwise it points to the position of the last expanded list
	 * item
	 */
	private int lastOpenPosition = -1;

	/**
	 * Default Animation duration Set animation duration with @see
	 * setAnimationDuration
	 */
	private int animationDuration = 330;

	/**
	 * A list of positions of all list items that are expanded. Normally only
	 * one is expanded. But a mode to expand multiple will be added soon.
	 * 
	 * If an item onj position x is open, its bit is set
	 */
	private BitSet openItems = new BitSet();
	/**
	 * We remember, for each collapsable view its height. So we dont need to
	 * recalculate. The height is calculated just before the view is drawn.
	 */
	private final SparseIntArray viewHeights = new SparseIntArray(10);

	/**
	 * Will point to the ListView
	 */
	private ViewGroup parent;

	// private BaseAdapter wrapped;
	public AbstractSlideExpandableListAdapter() {
		// this.wrapped = wrapped;
	}

	private OnItemExpandCollapseListener expandCollapseListener;

	/**
	 * Sets a listener which gets call on item expand or collapse
	 * 
	 * @param listener
	 *            the listener which will be called when an item is expanded or
	 *            collapsed
	 */
	public void setItemExpandCollapseListener(
			OnItemExpandCollapseListener listener) {
		expandCollapseListener = listener;
	}

	public void removeItemExpandCollapseListener() {
		expandCollapseListener = null;
	}

	/**
	 * Interface for callback to be invoked whenever an item is expanded or
	 * collapsed in the list view.
	 */
	public interface OnItemExpandCollapseListener {
		/**
		 * Called when an item is expanded.
		 * 
		 * @param itemView
		 *            the view of the list item
		 * @param position
		 *            the position in the list view
		 */
		public void onExpand(View itemView, int position);

		/**
		 * Called when an item is collapsed.
		 * 
		 * @param itemView
		 *            the view of the list item
		 * @param position
		 *            the position in the list view
		 */
		public void onCollapse(View itemView, int position);

	}

	private void notifiyExpandCollapseListener(int type, View view, int position) {
		if (expandCollapseListener != null) {
			if (type == ExpandCollapseAnimation.EXPAND) {
				expandCollapseListener.onExpand(view, position);
			} else if (type == ExpandCollapseAnimation.COLLAPSE) {
				expandCollapseListener.onCollapse(view, position);
			}
		}

	}

	// @Override
	// public View getView(int position, View view, ViewGroup viewGroup) {
	// this.parent = viewGroup;
	// // view = wrapped.getView(position, view, viewGroup);
	// enableFor(view, position);
	// return view;
	// }

	/**
	 * This method is used to get the Button view that should expand or collapse
	 * the Expandable View. <br/>
	 * Normally it will be implemented as:
	 * 
	 * <pre>
	 * return parent.findViewById(R.id.expand_toggle_button)
	 * </pre>
	 * 
	 * A listener will be attached to the button which will either expand or
	 * collapse the expandable view
	 * 
	 * @see #getExpandableView(View)
	 * @param parent
	 *            the list view item
	 * @ensure return!=null
	 * @return a child of parent which is a button
	 */
	public abstract View getExpandToggleButton(View parent);

	/**
	 * This method is used to get the view that will be hidden initially and
	 * expands or collapse when the ExpandToggleButton is pressed @see
	 * getExpandToggleButton <br/>
	 * Normally it will be implemented as:
	 * 
	 * <pre>
	 * return parent.findViewById(R.id.expandable)
	 * </pre>
	 * 
	 * @see #getExpandToggleButton(View)
	 * @param parent
	 *            the list view item
	 * @ensure return!=null
	 * @return a child of parent which is a view (or often ViewGroup) that can
	 *         be collapsed and expanded
	 */
	public abstract View getExpandableView(View parent);

	/**
	 * Gets the duration of the collapse animation in ms. Default is 330ms.
	 * Override this method to change the default.
	 * 
	 * @return the duration of the anim in ms
	 */
	public int getAnimationDuration() {
		return animationDuration;
	}

	/**
	 * Set's the Animation duration for the Expandable animation
	 * 
	 * @param duration
	 *            The duration as an integer in MS (duration > 0)
	 * @exception IllegalArgumentException
	 *                if parameter is less than zero
	 */
	public void setAnimationDuration(int duration) {
		if (duration < 0) {
			throw new IllegalArgumentException("Duration is less than zero");
		}

		animationDuration = duration;
	}

	/**
	 * Check's if any position is currently Expanded To collapse the open item @see
	 * collapseLastOpen
	 * 
	 * @return boolean True if there is currently an item expanded, otherwise
	 *         false
	 */
	public boolean isAnyItemExpanded() {
		return (lastOpenPosition != -1) ? true : false;
	}

	// public void enableFor(View parent, int position) {
	// View more = getExpandToggleButton(parent);
	// View itemToolbar = getExpandableView(parent);
	// itemToolbar.measure(parent.getWidth(), parent.getHeight());
	//
	// enableFor(more, itemToolbar, position);
	// itemToolbar.requestLayout();
	// }

	public void enableFor(final View button, final View buttonMore,
			final View targetTop, final View targetBottom, final int position,
			ViewGroup viewGroup) {
		this.parent = viewGroup;
		if (targetTop == lastOpen && position != lastOpenPosition) {
			// lastOpen is recycled, so its reference is false
			lastOpen = null;
		}
		if (targetBottom != null) {
			if (targetBottom == lastOpen && position != lastOpenPosition) {
				// lastOpen is recycled, so its reference is false
				lastOpen = null;
			}
		}
		if (position == lastOpenPosition) {
			// re reference to the last view
			// so when can animate it when collapsed
			if (targetTop.getVisibility() == View.VISIBLE) {
				lastOpen = targetTop;
			}
			if (targetBottom.getVisibility() == View.VISIBLE) {
				lastOpen = targetBottom;
			}
		}
		int height = viewHeights.get(position, -1);
		if (height == -1) {
			viewHeights.put(position, targetTop.getMeasuredHeight());
			updateExpandable(targetTop, position);
			if (targetBottom != null) {
				viewHeights.put(position, targetBottom.getMeasuredHeight());
				updateExpandable(targetBottom, position);
			}
		} else {
			updateExpandable(targetTop, position);
			if (targetBottom != null) {
				updateExpandable(targetBottom, position);
			}
		}
		if (targetBottom != null) {
			button.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(final View view) {

					Animation a = targetBottom.getAnimation();

					if (a != null && a.hasStarted() && !a.hasEnded()) {

						a.setAnimationListener(new Animation.AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								view.performClick();
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
							}
						});

					} else {

						targetBottom.setAnimation(null);

						int type = targetBottom.getVisibility() == View.VISIBLE ? ExpandCollapseAnimation.COLLAPSE
								: ExpandCollapseAnimation.EXPAND;

						// remember the state
						if (type == ExpandCollapseAnimation.EXPAND) {
							openItems.set(position, true);
						} else {
							openItems.set(position, false);
						}
						// check if we need to collapse a different view
						if (type == ExpandCollapseAnimation.EXPAND) {
							if (lastOpenPosition != -1
									&& lastOpen != targetBottom) {
								if (lastOpen != null) {
									animateView(lastOpen,
											ExpandCollapseAnimation.COLLAPSE);
									notifiyExpandCollapseListener(
											ExpandCollapseAnimation.COLLAPSE,
											lastOpen, lastOpenPosition);
								}
								openItems.set(lastOpenPosition, false);
							}
							lastOpen = targetBottom;
							lastOpenPosition = position;
						} else if (lastOpenPosition == position) {
							lastOpenPosition = -1;
						}
						animateView(targetBottom, type);
						notifiyExpandCollapseListener(type, targetBottom,
								position);
					}
					return true;
				}
			});
		}

		buttonMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				Animation a = targetTop.getAnimation();

				if (a != null && a.hasStarted() && !a.hasEnded()) {

					a.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {
						}

						@Override
						public void onAnimationEnd(Animation animation) {
							view.performClick();
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
						}
					});

				} else {

					targetTop.setAnimation(null);

					int type = targetTop.getVisibility() == View.VISIBLE ? ExpandCollapseAnimation.COLLAPSE
							: ExpandCollapseAnimation.EXPAND;

					// remember the state
					if (type == ExpandCollapseAnimation.EXPAND) {
						openItems.set(position, true);
					} else {
						openItems.set(position, false);
					}
					// check if we need to collapse a different view
					if (type == ExpandCollapseAnimation.EXPAND) {
						if (lastOpenPosition != -1 && lastOpen != targetTop) {
							if (lastOpen != null) {
								animateView(lastOpen,
										ExpandCollapseAnimation.COLLAPSE);
								notifiyExpandCollapseListener(
										ExpandCollapseAnimation.COLLAPSE,
										lastOpen, lastOpenPosition);
							}
							openItems.set(lastOpenPosition, false);
						}
						lastOpen = targetTop;
						lastOpenPosition = position;
					} else if (lastOpenPosition == position) {
						lastOpenPosition = -1;
					}
					animateView(targetTop, type);
					notifiyExpandCollapseListener(type, targetTop, position);
				}
				if(view.getTag() != null ){
					BaseAdapter adapter = (BaseAdapter) view.getTag();
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void updateExpandable(View target, int position) {

		final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) target
				.getLayoutParams();
		if (openItems.get(position)) {
			target.setVisibility(View.VISIBLE);
			params.bottomMargin = 0;
		} else {
			target.setVisibility(View.GONE);
			params.bottomMargin = 0 - viewHeights.get(position);
		}
	}

	/**
	 * Performs either COLLAPSE or EXPAND animation on the target view
	 * 
	 * @param target
	 *            the view to animate
	 * @param type
	 *            the animation type, either ExpandCollapseAnimation.COLLAPSE or
	 *            ExpandCollapseAnimation.EXPAND
	 */
	private void animateView(final View target, final int type) {
		Animation anim = new ExpandCollapseAnimation(target, type);
		anim.setDuration(getAnimationDuration());
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (type == ExpandCollapseAnimation.EXPAND) {
					if (parent instanceof ListView) {
						ListView listView = (ListView) parent;
						int movement = target.getBottom();

						Rect r = new Rect();
						boolean visible = target.getGlobalVisibleRect(r);
						Rect r2 = new Rect();
						listView.getGlobalVisibleRect(r2);

						if (!visible) {
							listView.smoothScrollBy(movement,
									getAnimationDuration());
						} else {
							if (r2.bottom == r.bottom) {
								listView.smoothScrollBy(movement,
										getAnimationDuration());
							}
						}
					}
				}

			}
		});
		target.startAnimation(anim);
	}

	/**
	 * Closes the current open item. If it is current visible it will be closed
	 * with an animation.
	 * 
	 * @return true if an item was closed, false otherwise
	 */
	public boolean collapseLastOpen() {
		if (isAnyItemExpanded()) {
			// if visible animate it out
			if (lastOpen != null) {
				animateView(lastOpen, ExpandCollapseAnimation.COLLAPSE);
			}
			openItems.set(lastOpenPosition, false);
			lastOpenPosition = -1;
			return true;
		}
		return false;
	}

	public Parcelable onSaveInstanceState(Parcelable parcelable) {

		SavedState ss = new SavedState(parcelable);
		ss.lastOpenPosition = this.lastOpenPosition;
		ss.openItems = this.openItems;
		return ss;
	}

	public void onRestoreInstanceState(SavedState state) {

		if (state != null) {
			this.lastOpenPosition = state.lastOpenPosition;
			this.openItems = state.openItems;
		}
	}

	/**
	 * Utility methods to read and write a bitset from and to a Parcel
	 */
	private static BitSet readBitSet(Parcel src) {
		BitSet set = new BitSet();
		if (src == null) {
			return set;
		}
		int cardinality = src.readInt();

		for (int i = 0; i < cardinality; i++) {
			set.set(src.readInt());
		}

		return set;
	}

	private static void writeBitSet(Parcel dest, BitSet set) {
		int nextSetBit = -1;

		if (dest == null || set == null) {
			return; // at least dont crash
		}

		dest.writeInt(set.cardinality());

		while ((nextSetBit = set.nextSetBit(nextSetBit + 1)) != -1) {
			dest.writeInt(nextSetBit);
		}
	}

	/**
	 * The actual state class
	 */
	static class SavedState extends View.BaseSavedState {
		public BitSet openItems = null;
		public int lastOpenPosition = -1;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			lastOpenPosition = in.readInt();
			openItems = readBitSet(in);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(lastOpenPosition);
			writeBitSet(out, openItems);
		}

		// required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}
