HzScrolling = function(html) {
  this._$html = html;
  this._finger_start_x = undefined;
  this._finger_diff_x = 0;
  this._mouse_is_down = false;
  this._x = 0;
  this._idx = 0;
  
  // should be consistent with CSS
  this._item_margin_right = 5;

  // array to hold the width of each item you add
  this._widths = [127,127,127,127,127,127,127,127];
};



HzScrolling.prototype = {
  html: function() {
    return this._$html;
  },
  
  
  add_item: function(name, width) {
    if (!width) {
      width = 64;
    }
    this._$html.find('.recommendations_scroller').append('<img class="recommendation" src="img/my_city/' + name + '.png" style="height: 120px; width: ' + width + 'px;">');
    this._widths.push(width);
  },
  
  
  bind: function() {
    var self = this;
    var $scroller = this._$html.find('.scroller');
    
/*
    this._$html.find('.thumb_up').bind('touchend mouseup', function() {
      self.move_left();
    });

    this._$html.find('.thumb_down').bind('touchend mouseup', function() {
      self.move_left();
    });
*/
    
    $scroller.bind('touchstart mousedown', function(e) {
      $scroller.css('-webkit-transition', 'all 0');

      self._finger_diff_x = 0;
      self._finger_start_x = e.pageX;
      self._mouse_is_down = true;
    });
    
    
    
    $scroller.bind('touchmove mousemove', function(e) {
      if (!self._mouse_is_down) {
        return;
      }
      
      self._finger_diff_x = e.pageX - self._finger_start_x;
      
      $scroller.css('-webkit-transform', 'translate3d(' + (self._x + self._finger_diff_x) + 'px, 0px, 0px)');
    });
    
    
    
    $scroller.bind('touchend mouseup', function() {
      self._mouse_is_down = false;

      if (self._finger_diff_x == 0) {
        return;      
      }

      if (self._finger_diff_x > 10) {
        self.move_right();
      } else if (self._finger_diff_x < -10) {
        self.move_left();
      } else {
        $scroller.css('-webkit-transition', 'all 300ms');
        $scroller.css('-webkit-transform', 'translate3d(' + self._x + 'px, 0px, 0px)');
      }

    });
  },
  
  move_left: function() {
    this._$html.find('.scroller').css('-webkit-transition', 'all 300ms');
    var new_x = 0;
    this._idx++;
  
    if (this._idx >= this._widths.length) {
      this._idx--;
    }
    
    for (var i=0; i<this._idx; i++) {
      new_x -= (this._widths[i] + this._item_margin_right);
    }

    this._x = new_x;
    this._$html.find('.scroller').css('-webkit-transform', 'translate3d(' + this._x + 'px, 0px, 0px)');
  },
  
  move_right: function() {
    this._$html.find('.scroller').css('-webkit-transition', 'all 300ms');
    var new_x = 0;
    this._idx--;
  
    if (this._idx < 0) {
      this._idx++;
    }
    
    for (var i=0; i<this._idx; i++) {
      new_x -= (this._widths[i] + this._item_margin_right);
    }

    this._x = new_x;
    this._$html.find('.scroller').css('-webkit-transform', 'translate3d(' + this._x + 'px, 0px, 0px)');

  }
};
