package com.alfred.callnum.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.alfred.callnum.R;
import com.alfred.callnum.activity.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CallNumUtil {
    /*
     * AudioTrack主要用于播放叫号：号码（字母和数字组成）音频数据PCM预先从文件加载到内存中。
     */
    static AudioTrack audioTrack = null;
    static volatile boolean g_bAudioPlaying = false;

    static AudioManager mAudioManager;
    private static MediaPlayer mediaPlayer = null;

    // static byte[] mchars=new byte[10];
    /*
     * 用于播放其余语音文件，包括前缀，后缀，自定义语音文件
     */
    private static MediaPlayer mplayer = null;
    private static int mindex;

    public static boolean isPlay = false;


    /*
     * 叫号/播放消息队列
     */
    static final int BUFFER_SIZE = 16;
    static final CallNumQueueUtil[] mPlayTaskList = new CallNumQueueUtil[BUFFER_SIZE];
    static volatile int mTail = 0;
    static volatile int mHead = 0;// 主线程和异步线程共享

    static final String TAG = CallNumUtil.class.getSimpleName();

    static Context mCxt;
    static String mstrCustWavPath;
    static boolean mbHasCustWave;// 个性化配置
    static boolean mbHasBluetoothAudio;
    //	AudioManager mAudioManager;
    // 最大音量
    private static int maxVolume;
    public static Handler mMsgHandler = null;
    public static final int MSG_TYPE_START_CALL = 0x500;
    public static final int MSG_TYPE_END_CALL = 0x501;
    static List<Integer> mchars = new ArrayList<Integer>();

//	private static ShopInfo shop;

    /**
     * handler的用处
     * case CallNum.MSG_TYPE_START_CALL: {
     * TODO 开始叫号的消息
     * }
     * break;
     * case CallNum.MSG_TYPE_END_CALL: {
     * TODO 结束叫号的消息
     * }
     * break;
     *
     * @param c
     * @param handler
     */
    public static void init(Context c, Handler handler) {
        mCxt = c;
        mMsgHandler = handler;
        mbHasBluetoothAudio = false;
        mAudioManager = (AudioManager) mCxt.getSystemService(Context.AUDIO_SERVICE);
        initCallWav();
        initAudioTrack();
//		shop = shopInfo;
        AudBluetoothEn(true);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new CompletionListener());
    }

    public static void AudBluetoothEn(boolean en) {
        mbHasBluetoothAudio = en;
    }

    /**
     * 初始化音量
     */
    public static void initVideo(Context c) {

        mAudioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
                AudioManager.FLAG_PLAY_SOUND);
//		mAudioManager.adjustSuggestedStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
//				AudioManager.FLAG_PLAY_SOUND);

//		LogUtil.d("LXX", "当前音量："+current);
        if (current <= 1)
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume / 2, 0); //tempVolume:音量绝对值
//		mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_PLAY_SOUND);
//		LogUtil.d("LXX", "当前音量："+current);

		/*mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// 最大音量
		maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int v = mApp.getVideoVol();
		if (v >= 0) {
			LogFile.w("上次叫号未完成");
			int tempvol = (int) (maxVolume * v / 100 + 0.5);
			mAudioManager
					.setStreamVolume(AudioManager.STREAM_MUSIC, tempvol, 0); // tempVolume:音量绝对值
		}
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); // tempVolume:音量绝对值
		 */
    }

    static List<byte[]> chars_list;
    static List<byte[]> chars2_list;
    static List<byte[]> nums_list;
    static List<byte[]> nums2_list;
    static byte[] vipdata = null;
    static byte[] haodata = null;
    static byte[] enddata = null;
    static byte[] please = null;
    static byte[] dao = null;
    static byte[] chu = null;
    static byte[] can = null;
    static byte[] kou = null;
    static byte[] qu = null;
    static byte[] ba = null;
    static byte[] tai = null;

    public static void initCallWav() {
        mbHasCustWave = false;
//		String root = Environment.getExternalStorageDirectory()
//				+ File.separator + "paidui";
//		File filer = new File(root);
//		if (filer.exists()) {
////			mstrCustWavPath = ShopInfo.getCustWavPath();
//			File custwave = new File(mstrCustWavPath);
//			if (custwave.exists()) {
//				File[] files = custwave.listFiles();
//				if (files != null && files.length > 10) {
//					mbHasCustWave = true;
////					Log.i(String.format("使用自定义语音包,file count=%d",
////							files.length));
//				}
//			}
//		} else {
//			filer.mkdir();
//		}

        chars_list = new ArrayList<byte[]>();
        nums_list = new ArrayList<byte[]>();
        chars2_list = new ArrayList<byte[]>();
        nums2_list = new ArrayList<byte[]>();
        vipdata = null;
        haodata = null;

        Thread loadwave = new Thread(new Runnable() {

            @Override
            public void run() {
                if (mbHasCustWave) {
                    load_custom();
                } else {
                    // load_default();
                }

            }
        });
        loadwave.start();
    }

    final static int WAV_FILE_HEADER_LEN = 0x2c;

    static int wave_len(byte[] buff) {
        return ((buff[0x2b] & 0xff) << 24) | ((buff[0x2a] & 0xff) << 16)
                | ((buff[0x29] & 0xff) << 8) | (buff[0x28] & 0xff);
    }


    /**
     * 获得指定文件的byte数组
     */
    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    static byte[] readPCM(File file) throws FileNotFoundException {
        InputStream in = new FileInputStream(file);

        byte[] data = readPCM(in);
        if (data == null) {
//			LogFile.e(String.format("wavfile=%s err", file.getName()));
        }
        return data;
    }


    static byte[] readPCMString(String file) throws FileNotFoundException {

        WaveFileReader reader = new WaveFileReader(file);
        byte[] data = intToBytes(reader.getMyData()[0]);
        if (data == null) {
//			LogFile.e(String.format("wavfile=%s err", file.getName()));
        }
        return data;
    }

    public static byte[] intToBytes(int n) {
        String s = String.valueOf(n);
        return s.getBytes();
    }


    static byte[] readPCM(InputStream in) {
        try {

//			ByteBuffer buffer = ByteBuffer.allocate(WAV_FILE_HEADER_LEN);
//			buffer.order(ByteOrder.LITTLE_ENDIAN);
//
//			in.read(buffer.array(), buffer.arrayOffset(), buffer.capacity());
//
//			buffer.rewind();
//			buffer.position(buffer.position() + 20);
//			int format = buffer.getShort();
//			//checkFormat(format == 1,  // 1 means Linear PCM
//			Log.e("TAG", "Unsupported encoding: " + format);
//			int channels = buffer.getShort();
//			//checkFormat(channels == 1 || channels == 2, "Unsupported channels: " + channels);
//			Log.e("TAG", "Unsupported channels: " + channels);
//			int rate = buffer.getInt();
////			checkFormat(rate <= 48000 && rate >= 11025, "Unsupported rate: " + rate);
//			Log.e("TAG", "Unsupported rate: " + rate);
//			buffer.position(buffer.position() + 6);
//			int bits = buffer.getShort();
////			checkFormat(bits == 16, "Unsupported bits: " + bits);
//			Log.e("TAG", "Unsupported bits: " + bits);

            byte[] wave_header_buff = new byte[WAV_FILE_HEADER_LEN];
            int len = in.read(wave_header_buff);// wave文件头
            if (len < WAV_FILE_HEADER_LEN) {
//				LogUtil.d("LXX" ,"文件读取失败read_len=" + len);
                return null;
            }
            // 得到数据的大小


            int wav_len = wave_len(wave_header_buff);
            if (wav_len <= 0 || wav_len > 1024 * 1024)// 1M
            {
                Log.d("LXX", "文件读取失败：wav_len=" + wav_len);
                return null;// 文件格式错误
            }
            byte[] buffers = new byte[wav_len];

            // 读取数据
            in.read(buffers);

            // 关闭
            in.close();

            return buffers;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("LXX", "转换字节数组出错");
        }
        return null;
    }

    public static byte[] inputStream2byte(InputStream in) {
        try {
            int length = in.available();
            byte[] tmp = new byte[length];
            in.read(tmp);
			/*ByteArrayOutputStream out = new ByteArrayOutputStream();
			int bytesRead = in.read(tmp);
			while (bytesRead != -1) {
				out.write(tmp, 0, bytesRead);
				bytesRead = in.read(tmp);
			}
			return out.toByteArray();
			*/
            return tmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;

        final String scheme = uri.getScheme();
        String data = null;

        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    //	public static String getRealFilePath( final Context context, final Uri uri ) {
//		if ( null == uri ) return null;
//		final String scheme = uri.getScheme();
//		String data = null;
//		if ( scheme == null )
//			data = uri.getPath();
//		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
//			data = uri.getPath();
//		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
//			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
//			if ( null != cursor ) {
//				if ( cursor.moveToFirst() ) {
//					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
//					if ( index > -1 ) {
//						data = cursor.getString( index );
//					}
//				}
//				cursor.close();
//			}
//		}
//		return data;
//	}
//    static void load_default() {
//        for (int resid : chars_resid) {
//
////			for (byte c = 'A'; c <= 'I'; c++) {
////				try {
////
////					Uri uri=Uri.parse("android.resource://" + mCxt.getPackageName() + "/" +R.raw.a);
////
////					String	filepath ="a.wav";
////
//////					Uri filepath = "android.resource://" + mCxt.getPackageName() + "/" +resid;
//////				      String	  filepath =getFilePath(mCxt,uri);
////					//String filepath = String.format("%c.wav", c);
////						// 得到wav数据流
////					//chars_list.add(readPCMString(filepath));
////
////					InputStream in = mCxt.getResources().openRawResource(resid);
////    				chars_list.add(input2byte(in));
////
////				} catch (Exception e) {
////				}
////			}
//            try {
//                // 得到资源中的Raw数据流
//                InputStream in = mCxt.getResources().openRawResource(resid);
//                chars_list.add(readPCM(in));
//            } catch (Exception e) {
//                chars_list.add(null);
//            }
//        }
//        try {
//            // 得到资源中的Raw数据流
//            InputStream in = mCxt.getResources().openRawResource(R.raw.v);
//            vipdata = readPCM(in);
//        } catch (Exception e) {
//        }
//
//        try {
//            // 得到资源中的Raw数据流
//            //InputStream in = mCxt.getResources().openRawResource(R.raw.hao_end1);
//            InputStream in = mCxt.getResources().openRawResource(R.raw.hao_end);
//            enddata = readPCM(in);
////			enddata = inputStream2byte(in);
//        } catch (Exception e) {
//            Log.d("LXX", "hao_end读取异常");
//            e.printStackTrace();
//        }
//
//        try {
//            // 得到资源中的Raw数据流
////            InputStream in = mCxt.getResources().openRawResource(R.raw.can);
////            can = readPCM(in);
//        } catch (Exception e) {
//            Log.d("LXX", "can读取异常");
//            e.printStackTrace();
//        }
//        try {
//            // 得到资源中的Raw数据流
//            InputStream in = mCxt.getResources().openRawResource(R.raw.chu);
//            chu = readPCM(in);
//        } catch (Exception e) {
//            Log.d("LXX", "chu读取异常");
//            e.printStackTrace();
//        }
//        try {
//            // 得到资源中的Raw数据流
////            InputStream in = mCxt.getResources().openRawResource(R.raw.kou);
////            kou = readPCM(in);
//        } catch (Exception e) {
//            Log.d("LXX", "chu读取异常");
//            e.printStackTrace();
//        }
//        try {
//            // 得到资源中的Raw数据流
////            InputStream in = mCxt.getResources().openRawResource(R.raw.please);
////            please = readPCM(in);
//        } catch (Exception e) {
//            Log.d("LXX", "chu读取异常");
//            e.printStackTrace();
//        }
//        try {
//            // 得到资源中的Raw数据流
////            InputStream in = mCxt.getResources().openRawResource(R.raw.dao);
////            dao = readPCM(in);
//        } catch (Exception e) {
//            Log.d("LXX", "chu读取异常");
//            e.printStackTrace();
//        }
//        try {
//            // 得到资源中的Raw数据流
//            InputStream in = mCxt.getResources().openRawResource(R.raw.qu);
//            qu = readPCM(in);
//        } catch (Exception e) {
//            Log.d("LXX", "chu读取异常");
//            e.printStackTrace();
//        }
//        try {
//            // 得到资源中的Raw数据流
////            InputStream in = mCxt.getResources().openRawResource(R.raw.ba);
////            ba = readPCM(in);
//        } catch (Exception e) {
//            Log.d("LXX", "chu读取异常");
//            e.printStackTrace();
//        }
//        try {
//            // 得到资源中的Raw数据流
////            InputStream in = mCxt.getResources().openRawResource(R.raw.tai);
////            tai = readPCM(in);
//        } catch (Exception e) {
//            Log.d("LXX", "chu读取异常");
//            e.printStackTrace();
//        }
//
//
//        for (int resid : nums_resid) {
//
//
//            try {
////					Uri uri=Uri.parse("android.resource://" + mCxt.getPackageName() + "/" +resid);
////
////					String	filepath =getFilePath(mCxt,uri);
////					InputStream in = mCxt.getResources().openRawResource(resid);
////					chars_list.add(input2byte(in));
////					String filepath = String.format("n%c.wav", c);
//                InputStream in = mCxt.getResources().openRawResource(resid);
//                nums_list.add(input2byte(in));
//            } catch (Exception e) {
////				LogFile.e("load wav", e);
//            }
//        }
//
//
////			try {
////				// 得到资源中的Raw数据流
////				InputStream in = mCxt.getResources().openRawResource(resid);
////				nums_list.add(input2byte(in));
////
////
////			} catch (Exception e) {
////				nums_list.add(null);
////			}
////		}
//
//    }

    private static void initWAV(byte[] result, int res) {
        try {
            // 得到资源中的Raw数据流
            //InputStream in = mCxt.getResources().openRawResource(R.raw.hao_end1);
            result = readPCM(mCxt.getResources().openRawResource(res));
        } catch (Exception e) {
            Log.d("LXX", "hao_end读取异常");
            e.printStackTrace();
        }
    }

    static boolean mbCallNumIn2ndLang = false;

    static void load_custom() {
        for (byte c = 'A'; c <= 'I'; c++) {
            try {
                String filepath = mstrCustWavPath + File.separator
                        + String.format(Locale.US,"%c.mp3", c);
                File wavfile = new File(filepath);
                if (wavfile.exists()) {
                    // 得到wav数据流
                    chars_list.add(readPCM(wavfile));
                } else {
                    chars_list.add(null);// 文件不存在
                }
                String filepath2 = mstrCustWavPath + File.separator
                        + String.format(Locale.US,"e%c.wav", c);
                File wavfile2 = new File(filepath2);
                if (wavfile2.exists()) {
                    // 得到wav数据流
                    chars2_list.add(readPCM(wavfile2));
                } else {
                    chars2_list.add(null);// 文件不存在
                }
            } catch (Exception e) {
            }
        }
        vipdata = null;
        try {
            String filepath = mstrCustWavPath + File.separator + "v.wav";
            File wavfile = new File(filepath);
            if (wavfile.exists()) {
                // 得到资源中的Raw数据流
                vipdata = readPCM(wavfile);
            }
        } catch (Exception e) {
        }
        try {
            String filepath = mstrCustWavPath + File.separator + "hao.wav";
            File wavfile = new File(filepath);
            if (wavfile.exists()) {
                // 得到资源中的Raw数据流
                haodata = readPCM(wavfile);
            }
        } catch (Exception e) {
        }

        for (byte c = '0'; c <= '9'; c++) {
            try {
                String filepath = mstrCustWavPath + File.separator
                        + String.format(Locale.US,"n%c.mp3", c);
                File wavfile = new File(filepath);
                if (wavfile.exists()) {


                    nums_list.add(readPCM(wavfile));
                } else {
                    nums_list.add(null);// 文件不存在
//					LogFile.i(String.format("n%c.wav 不存在", c));
                }
                // 加载e语音
                String efilepath = mstrCustWavPath + File.separator
                        + String.format(Locale.US,"e%c.wav", c);
                File ewavfile = new File(efilepath);
                if (ewavfile.exists()) {
                    nums2_list.add(readPCM(ewavfile));
                } else {
                    // 文件不存在
                    // LogFile.i(String.format("e%c.wav 不存在",c));
                }

            } catch (Exception e) {
//				LogFile.e("load wav", e);
            }
        }

        mbCallNumIn2ndLang = nums2_list.size() >= 10;
    }

    static WavRes s_PlayingRes = null;
    static long s_StartPlayTime = 0;
    static long s_EndPlayTime = 0;

    public static void call(CallNumQueueUtil num) {
        g_bStoping = false;
        checkPlayer(num);
        if (g_bAudioPlaying) {
            enqueue(num);
        } else {
//			LogUtil.d("LXX",String.format("new Call:(%s,%s),cn=%d", num.queid,
//					num.value, num.called_num));
            if (mMsgHandler != null) {
                mMsgHandler.obtainMessage(MSG_TYPE_START_CALL).sendToTarget();// 通知视频暂停
            }

            int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//			LogUtil.d("LXX", "当前音量："+current);
            num.called_num--;
            start_call(num);
        }
    }

    static int quesize() {
        if (mTail >= mHead) {
            return mTail - mHead;
        } else {
            return BUFFER_SIZE + mTail - mHead;
        }
    }

    public static void enqueue(CallNumQueueUtil num) {
//		LogUtil.d("LXX",String.format("enqueue:(%s,%s),cn=%d,@%d", num.queid,
//				num.value, num.called_num, mTail));

        mPlayTaskList[mTail] = num;
        mTail++;
        mTail = mTail % BUFFER_SIZE;
    }

    public static CallNumQueueUtil dequeue() {
        if (mTail != mHead) {
            CallNumQueueUtil num = mPlayTaskList[mHead];
            mPlayTaskList[mHead] = null;// release
//			LogFile.i(String.format("dequeue:(%s,%s),cn=%d,@%d", num.queid,
//					num.value, num.called_num, mHead));

            mHead++;
            mHead = mHead % BUFFER_SIZE;
            if (num.called_num <= 0)// 取消了
            {
                return dequeue();
            }

            num.called_num--;
            return num;
        }

        return null;
    }

    static void release() {
        mplayer.stop();
        mplayer.release();
        mplayer = null;
        g_bAudioPlaying = false;
    }

    static void checkPlayer(CallNumQueueUtil num) {
        if (mplayer != null && mplayer.isPlaying()) {
            long deltat = System.currentTimeMillis() - s_StartPlayTime;
            long duration = mplayer.getDuration() + 2000;
            if (deltat > duration) {
//				LogUtil.d("LXX", String.format(
//						"play timeout:dura=%d,cost=%d,file type=%d", duration,
//						deltat, s_PlayingRes.type));

                release();

                return;
            }
            // 正在播放自定义文件，叫号时掐断之前的
            if (s_PlayingRes.type == TYPE_CUSTFILE && num.number > 0) {
                release();

                return;
            }
        }

    }

    public static void resetplay() {
        mTail = mHead = 0;// empty the queue
        if (mplayer != null && mplayer.isPlaying()) {
            release();
        }

    }

    static volatile boolean g_bStoping = false;

    public static void cancleCall() {
        g_bStoping = true;
        resetplay();
    }

    public static void cancleCall(CallNumQueueUtil num) {
        boolean bStop = true;
        if (mTail > mHead) {
            for (int i = mHead; i < mTail; i++) {
                CallNumQueueUtil numInQ = mPlayTaskList[i];
                if (numInQ != null) {
                    if (numInQ.value.equals(num.value)
                            && numInQ.queid.equals(num.queid)) {
                        numInQ.called_num = 0;// 不叫了
                    } else {
                        bStop = false;// 还有其他叫号
                    }
                }
            }
        } else if (mTail < mHead) {
            for (int i = mHead; i < mTail + BUFFER_SIZE; i++) {
                CallNumQueueUtil numInQ = mPlayTaskList[i % BUFFER_SIZE];
                if (numInQ != null) {
                    if (numInQ.value.equals(num.value)
                            && numInQ.queid.equals(num.queid)) {
                        numInQ.called_num = 0;// 不叫了
                    } else {
                        bStop = false;// 还有其他叫号
                    }
                }
            }
        }

        if (bStop) {
            g_bStoping = true;
        }
    }

    final static int[] chars_resid = {R.raw.a, R.raw.b, R.raw.c, R.raw.d};
    final static int[] nums_resid = {R.raw.n0, R.raw.n1, R.raw.n2, R.raw.n3,
            R.raw.n4, R.raw.n5, R.raw.n6, R.raw.n7, R.raw.n8, R.raw.n9, R.raw.a, R.raw.b, R.raw.c, R.raw.d};

    final static int SILENCE_LEN = 20;// n*10ms
    final static byte[] silence = new byte[882 * SILENCE_LEN];// 静音，数据为0
    final static int TYPE_PREF = 0;
    final static int TYPE_NUM = 1;
    final static int TYPE_NUM_TAIL = 2;
    final static int TYPE_END = 3;
    final static int TYPE_CUSTFILE = 4;

    public static class WavRes {
        public int type;// 0：前缀，1：号码，2：号码结束，3：后缀
        public int res_id;
        public String path;

        public WavRes(int res_id, int t) {
            this.type = t;
            this.res_id = res_id;
            this.path = null;
        }

        public WavRes(String path, int t) {
            this.type = t;
            this.res_id = 0;
            this.path = path;
        }
    }

    static CallNumQueueUtil mCurTask;

    // 开始叫号——前缀或者号码
    private static boolean start_call(CallNumQueueUtil task) {
        mCurTask = task;
        if (task.number > 0) {// 叫号

            WavRes startf = null;
            String startfilepath;

            if (mCurTask.callInEn) {
                startfilepath = mstrCustWavPath + File.separator + "estart.wav";
                File startfile = new File(startfilepath);
                if (startfile.exists()) {
                    startf = new WavRes(startfilepath, TYPE_PREF);// 个性化配置
                }
            } else {
//				ShopInfo shop = TvApp.getInstance().getShopInfo(); // 云端配置
//				if (shop != null) {
//					String file = shop.getCallPref();
//					if (file != null && new File(file).exists()) {
//						startf = new WavRes(file, TYPE_PREF);
//					}
//				}
            }

            if (startf == null)// 云端配置不存在
            {
                if (mbHasCustWave) {
                    startfilepath = mstrCustWavPath + File.separator
                            + "start.wav";

                    File startfile = new File(startfilepath);
                    if (startfile.exists()) {
                        startf = new WavRes(startfilepath, TYPE_PREF);// 个性化配置
                    }
                    // 个性化配置 中可以没有
                } else {
                    // startf=new WavRes(R.raw.please,TYPE_PREF); //请字在54版本去掉
                }
            }
            if (startf != null) {
                playFile(startf);
                return true;
            }

            int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//			LogUtil.d("LXX", "当前音量2："+current);

            // 没有前缀，开始播放号码
            callNum();
            return true;
        } else// 自定义语音
        {
            if (new File(task.value).exists()) {
                playFile(new WavRes(task.value, TYPE_CUSTFILE));
                return true;
            }
        }

        return false;
    }

    // 号到餐厅就餐OR自定义
    private static void play_tail() {
        WavRes endf = null;
//		ShopInfo shop = TvApp.getInstance().getShopInfo();
//		if (shop != null) {
//			if (mCurTask != null && mCurTask.custcallwav > 0) {
//				String file = shop.getCallWav(mCurTask.custcallwav - 1);// 多层楼叫号
//				if (new File(file).exists()) {
//					endf = new WavRes(file, TYPE_END);
//				}
//			}
//		}
        if (endf == null) {
            // 最后一遍用英语叫号
            if (mCurTask != null && mCurTask.called_num <= 0
                    && mCurTask.callInEn) {
                String endfilepath = mstrCustWavPath + File.separator
                        + "eend.wav";
                File startfile = new File(endfilepath);
                if (startfile.exists()) {
                    endf = new WavRes(endfilepath, TYPE_END);// 个性化配置
                }
            } else {
//				String file = shop.getCallTail();
//				if (file != null && new File(file).exists()) {
//					endf = new WavRes(file, TYPE_END);// 优先使用云端配置
//				}
            }
        }

        if (endf == null)// 云端没有定制
        {
            if (mbHasCustWave) {
                String endfilepath = mstrCustWavPath + File.separator
                        + "end.wav";

                File endfile = new File(endfilepath);
                if (endfile.exists()) {
                    endf = new WavRes(endfilepath, TYPE_END);// 个性化配置
                }
                // 个性化配置 中可以没有
            } else {
                //  endf = new WavRes(R.raw.hao_end, TYPE_END); // 默认
            }
        }
		/*if (endf != null && !g_bStoping) {
			playFile(endf);
		} else {
			next_call();
		}*/
        next_call();
    }

    private static void next_call() {
        CallNumQueueUtil num = dequeue();
        if (num != null) {
            start_call(num);
        } else {
            g_bAudioPlaying = false;
            if (mMsgHandler != null) {
                mMsgHandler.obtainMessage(MSG_TYPE_END_CALL).sendToTarget();//
            }
        }
    }

    private static void mplayer_end(WavRes wav) {
        s_EndPlayTime = System.currentTimeMillis();
        if (wav.type == TYPE_PREF) {
            callNum();// 开始叫号
        } else {
            // 下一个号码
            next_call();
        }
    }

    // 播放文件
    private static void playFile(final WavRes wav) {
        if (wav.path != null) {
            mplayer = MediaPlayer
                    .create(mCxt, Uri.fromFile(new File(wav.path)));
        } else {
            mplayer = MediaPlayer.create(mCxt, wav.res_id);
        }
        // MediaPlayer.create之后，进入prepared状态
        if (mplayer != null) {
            try {
                g_bAudioPlaying = true;
                s_PlayingRes = wav;

//				if (wav.type == TYPE_END) {
//					audioTrack.write(enddata,0,enddata.length);
//				}else{
//					mplayer.start();
//				}

                s_StartPlayTime = System.currentTimeMillis();
                mplayer.setOnErrorListener(new OnErrorListener() {

                    @Override
                    public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                        return true;
                    }
                });
                mplayer.setOnPreparedListener(new OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {

                        // 该型号电视棒在audioTrack之后启动mediaplayer会出现提前complete
                        if (android.os.Build.MODEL.startsWith("rk3066")) {
                            mp.start();
                        }

                        long prepared_time = System.currentTimeMillis();
                        s_StartPlayTime = prepared_time;
                    }
                });
                mplayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        long play_cost = System.currentTimeMillis() - s_StartPlayTime;
                        if (play_cost + 10 < mp.getDuration()) {
                            // 有些平板，比如三星，会提前给结束
                            mMsgHdr.postDelayed(mRunnable, mp.getDuration()
                                    - play_cost);
                        } else {
                            mplayer.release();
                            mplayer = null;
                            mplayer_end(wav);
                        }
                    }
                });

            } catch (IllegalStateException e) {
//				LogUtil.d("LXX", "语音文件播放错误");
                mplayer.release();
                mplayer = null;
                mplayer_end(wav);// 本次叫号结束
            }
        } else {
//			LogUtil.d("LXX","语音文件打开失败:");
            if (wav.path != null) {
//				LogFile.i(wav.path);
            }
            mplayer_end(wav);// 跳过本次，进行下一个
        }

    }

    static void callNum() {
        g_bAudioPlaying = true; // main thread

        if (mChildHandler != null) {
            mChildHandler.obtainMessage(1).sendToTarget();
        }
    }


    // 播放MP3文件
    private static void playNumMp3() {

//        if (mbHasBluetoothAudio) {// 蓝牙音箱
//            long silenceTime = System.currentTimeMillis() - s_EndPlayTime;// 静默时间
//            if (silenceTime > 6000) {
//                for (int i = 0; i < 3; i++) {
//                   // audioTrack.write(silence, 0, silence.length);// 200ms
//                }
//            }
//        }
        boolean bFirst = true;
        final int MAX_NUM = 2;
        CallNumQueueUtil[] num_played = new CallNumQueueUtil[MAX_NUM];
        int call_count = 0;
        while (mCurTask != null) {
            if (mCurTask.number > 0) {
                if (call_count < MAX_NUM) {
                    num_played[call_count] = mCurTask;
                }
                call_count++;
                String playstring = "" + Integer.parseInt(mCurTask.quenamevoice + mCurTask.value);// 桌名+号码
                byte[] chars = playstring.toUpperCase().getBytes(); // 全部大写
                mchars.clear();
                for (byte c : chars) {
                    byte[] data = null;
                    if (c == 'V') {
                        data = vipdata;
                    } else if (c >= '0' && c <= 'I') {

                        if (c >= 'A') {
                            int pos = c - 'A';

                            mchars.add(chars_resid[pos]);

                        } else {
                            int pos = c - '0';
                            mchars.add(nums_resid[pos]);
                        }
                    } else if (c >= '0' && c <= '9') {
                        int pos = c - '0';
                        if (mCurTask.callInEn) {
                            if (pos < nums2_list.size()) {
                                data = nums2_list.get(pos);
                            }
                            if (data == null) {
                                mMsgHdr.obtainMessage(
                                        1,
                                        String.format(Locale.US,
                                                "数字语音e%c.wav错误或缺失，请联系客服！", c))
                                        .sendToTarget();
                            }
                        } else {
                            if (pos < nums_list.size()) {
                                data = nums_list.get(pos);
                            }
                            if (data == null) {
                                mMsgHdr.obtainMessage(
                                        1,
                                        String.format(Locale.US,
                                                "数字语音n%c.wav错误或缺失，请联系客服！", c))
                                        .sendToTarget();
                            }
                        }
                    } else if (c == ',') {
                        data = enddata;
                    } else if (c == '!') {
                        data = please;
                    } else if (c == '@') {
                        data = dao;
                    } else if (c == '#') {
                        data = chu;
                    } else if (c == '$') {
                        data = can;
                    } else if (c == '%') {
                        data = kou;
                    } else if (c == '^') {
                        data = qu;
                    } else if (c == '&') {
                        data = ba;
                    } else if (c == '*') {
                        data = tai;
                    } else if (c == '-') {
                        if (!mCurTask.callInEn && haodata != null) {
                            data = haodata;
                        } else {
                            data = silence;// 批量叫号间隔
                        }
                    }
                    if (data != null && !g_bStoping) {
                        // long prepared_time=System.currentTimeMillis();
                        //    audioTrack.write(data, 0, data.length);
                        // long delteT=System.currentTimeMillis()-prepared_time;

                        // Log.e(TAG, String.format("audio write=%d", delteT));
                        /*
                         * write():blocking and return when the data has been
                         * transferred from the Java layer to the native layer
                         * and queued for playback.
                         */
                        // fill silence
                        //    audioTrack.write(silence, 0, 882 * 2);// 20ms
                    }
                }

                playMp3();
                g_bAudioPlaying = true;

            } else {
                // 放弃自定义语音
            }
            if (mCurTask.custcallwav > 0) {
                break;
            }
            if (mbCallNumIn2ndLang
                    && (mCurTask.called_num > 0 || mCurTask.callInEn)) {
                if (mCurTask.callInEn) {
                    break;
                } else {
                    mCurTask.callInEn = true;// 指明下次英语叫号
                }
            }
            mCurTask = dequeue();// 尝试下一个号
            if (mCurTask != null && mCurTask.index == 1 && !bFirst) {
                byte[] b = new byte[882 * 50];
                audioTrack.write(b, 0, b.length);// 与前一个号间隔
            }
            bFirst = false;
        }
        if (!(mCurTask != null && mCurTask.callInEn) && haodata != null) {
            //  audioTrack.write(haodata, 0, haodata.length);
        } else {
            //   audioTrack.write(silence, 0, silence.length);
        }
        //   audioTrack.write(silence, 0, silence.length);


        //  audioTrack.stop();

        if (mMsgHandler != null) {
            mMsgHandler.obtainMessage(MainActivity.TYPE_AGAIN_CALL).sendToTarget();// 通知视频暂停
        }
        if (call_count <= MAX_NUM && !g_bStoping)// 连叫号码少的情况下重播
        {
            for (int i = 0; i < call_count; i++) {
                if (num_played[i].called_num > 0) {
                    enqueue(num_played[i]);
                }
            }
        }


    }

    private static void playNum() {
        //  audioTrack.play();
        if (mbHasBluetoothAudio) {// 蓝牙音箱
            long silenceTime = System.currentTimeMillis() - s_EndPlayTime;// 静默时间
            if (silenceTime > 6000) {
                for (int i = 0; i < 3; i++) {
                    audioTrack.write(silence, 0, silence.length);// 200ms
                }
            }
        }
        boolean bFirst = true;
        final int MAX_NUM = 2;
        CallNumQueueUtil[] num_played = new CallNumQueueUtil[MAX_NUM];
        int call_count = 0;
        while (mCurTask != null) {
            if (mCurTask.number > 0) {
                if (call_count < MAX_NUM) {
                    num_played[call_count] = mCurTask;
                }
                call_count++;
                if (!bFirst) {
                    audioTrack.write(silence, 0, silence.length);// 与前一个号间隔
                }
                String playstring = mCurTask.quenamevoice + mCurTask.value;// 桌名+号码
//				LogFile.i(String.format("playstring=%s,callidx=%d", playstring,
//						mCurTask.custcallwav));
                byte[] chars = playstring.toUpperCase().getBytes(); // 全部大写
                for (byte c : chars) {
                    byte[] data = null;
                    if (c == 'V') {
                        data = vipdata;
                    } else if (c >= 'A' && c <= 'I') {
                        int pos = c - 'A';
                        if (pos < chars_list.size()) {
                            if (mCurTask.callInEn) {
                                data = chars2_list.get(pos);// 优先查询配置的语音

                            }
                            if (data == null) {
                                data = chars_list.get(pos);
                            }
                        }
                        if (data == null) {
                            mMsgHdr.obtainMessage(1,
                                    String.format(Locale.US,"字母语音%c.wav错误或缺失，请联系客服！", c))
                                    .sendToTarget();
                        }
                    } else if (c >= '0' && c <= '9') {
                        int pos = c - '0';
                        if (mCurTask.callInEn) {
                            if (pos < nums2_list.size()) {
                                data = nums2_list.get(pos);
                            }
                            if (data == null) {
                                mMsgHdr.obtainMessage(
                                        1,
                                        String.format(Locale.US,
                                                "数字语音e%c.wav错误或缺失，请联系客服！", c))
                                        .sendToTarget();
                            }
                        } else {
                            if (pos < nums_list.size()) {
                                data = nums_list.get(pos);
                            }
                            if (data == null) {
                                mMsgHdr.obtainMessage(
                                        1,
                                        String.format(Locale.US,
                                                "数字语音n%c.wav错误或缺失，请联系客服！", c))
                                        .sendToTarget();
                            }
                        }
                    } else if (c == ',') {
                        data = enddata;
                    } else if (c == '!') {
                        data = please;
                    } else if (c == '@') {
                        data = dao;
                    } else if (c == '#') {
                        data = chu;
                    } else if (c == '$') {
                        data = can;
                    } else if (c == '%') {
                        data = kou;
                    } else if (c == '^') {
                        data = qu;
                    } else if (c == '&') {
                        data = ba;
                    } else if (c == '*') {
                        data = tai;
                    } else if (c == '-') {
                        if (!mCurTask.callInEn && haodata != null) {
                            data = haodata;
                        } else {
                            data = silence;// 批量叫号间隔
                        }
                    }
                    if (data != null && !g_bStoping) {
                        // long prepared_time=System.currentTimeMillis();
                        audioTrack.write(data, 0, data.length);
                        // long delteT=System.currentTimeMillis()-prepared_time;

                        // Log.e(TAG, String.format("audio write=%d", delteT));
                        /*
                         * write():blocking and return when the data has been
                         * transferred from the Java layer to the native layer
                         * and queued for playback.
                         */
                        // fill silence
                        audioTrack.write(silence, 0, 882 * 2);// 20ms
                    }
                }
            } else {
                // 放弃自定义语音
            }
            if (mCurTask.custcallwav > 0) {
                break;
            }
            if (mbCallNumIn2ndLang
                    && (mCurTask.called_num > 0 || mCurTask.callInEn)) {
                if (mCurTask.callInEn) {
                    break;
                } else {
                    mCurTask.callInEn = true;// 指明下次英语叫号
                }
            }
            mCurTask = dequeue();// 尝试下一个号
            if (mCurTask != null && mCurTask.index == 1 && !bFirst) {
                byte[] b = new byte[882 * 50];
                audioTrack.write(b, 0, b.length);// 与前一个号间隔
            }
            bFirst = false;
        }
        if (!(mCurTask != null && mCurTask.callInEn) && haodata != null) {
            audioTrack.write(haodata, 0, haodata.length);
        } else {
            audioTrack.write(silence, 0, silence.length);
        }
        audioTrack.write(silence, 0, silence.length);


        audioTrack.stop();

        if (mMsgHandler != null) {
            mMsgHandler.obtainMessage(MainActivity.TYPE_AGAIN_CALL).sendToTarget();// 通知视频暂停
        }
        if (call_count <= MAX_NUM && !g_bStoping)// 连叫号码少的情况下重播
        {
            for (int i = 0; i < call_count; i++) {
                if (num_played[i].called_num > 0) {
                    enqueue(num_played[i]);
                }
            }
        }


    }

    static void initAudioTrack() {
        if (audioTrack == null) {

            /*
             * AudioManager audioManager = (AudioManager)
             * mCxt.getSystemService(Context.AUDIO_SERVICE); int max =
             * audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
             * audioManager
             * .adjustSuggestedStreamVolume(AudioManager.STREAM_SYSTEM,
             * AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND); int
             * current =
             * audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
             * audioManager
             * .adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_PLAY_SOUND
             * );
             */
            int minBufferSize = AudioTrack.getMinBufferSize(0xac44,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);


            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 0xac44,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, minBufferSize * 2,
                    AudioTrack.MODE_STREAM);
            audioTrack.setStereoVolume(1.0f, 1.0f);// 设置当前音量大小
        }

        if (mChildHandler == null) {
            Thread audioThread = new audioTrackThread();
            audioThread.setPriority(Thread.MAX_PRIORITY - 2);
            audioThread.start();
        }
    }


    private static final class CompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            nextMp3();
        }

    }

    private static void nextMp3() {

        if (mindex < mchars.size() - 1) {
            mindex = mindex + 1;
            playMp3();
        } else {
            mindex = 0;

            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            isPlay = false;
            //   mCurTask = dequeue();// 尝试下一个号

//            if (mCurTask.custcallwav > 0) {
//                break;
//            }
//            if (mbCallNumIn2ndLang
//                    && (mCurTask.called_num > 0 || mCurTask.callInEn)) {
//                if (mCurTask.callInEn) {
//                    break;
//                } else {
//                    mCurTask.callInEn = true;// 指明下次英语叫号
//                }
//            }
            //    mCurTask = dequeue();// 尝试下一个号
//            if (mCurTask != null && mCurTask.index == 1 ) {
//                byte[] b = new byte[882 * 50];
//                audioTrack.write(b, 0, b.length);// 与前一个号间隔
//            }
            //   bFirst = false;
        }
    }

    //播放单个字母或数字
    static void playMp3() {
        isPlay = true;
        AssetFileDescriptor file = mCxt.getResources().openRawResourceFd(mchars.get(mindex));
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            mediaPlayer.prepare();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setVolume(1f, 1f);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    private static Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mplayer != null) {
                mplayer.release();
                mplayer = null;
                mplayer_end(s_PlayingRes);
            }
        }
    };

    private static Handler mMsgHdr = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    Toast.makeText(mCxt, (String) msg.obj, Toast.LENGTH_LONG)
                            .show();
                }
                break;
                case 2: {
                    // audioTrack.stop();
                    play_tail();// xxx请就餐
                }
                break;
            }
        }
    };

    static Handler mChildHandler = null;

    static class audioTrackThread extends Thread {

        public void run() {

            /*
             * 创建 handler前先初始化Looper.
             */
            Looper.prepare();

            /*
             * 在子线程创建handler，所以会绑定到子线程的消息队列中
             */
            mChildHandler = new Handler() {

                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        //    playNum();
                        playNumMp3();
                        mMsgHdr.obtainMessage(2).sendToTarget();
                    }
                }
            };

            /*
             * 启动该线程的消息队列
             */
            Looper.loop();
        }
    }
}
