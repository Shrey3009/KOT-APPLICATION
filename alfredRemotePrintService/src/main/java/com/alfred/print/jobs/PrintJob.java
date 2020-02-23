package com.alfred.print.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.alfred.printer.ESCPrinter;
import com.alfred.printer.PrintData;
import com.alfred.printer.PrintTscData;
import com.alfred.printer.StringUtil;
import com.alfred.remote.printservice.PrintService;
import com.alfredbase.ParamConst;
import com.alfredbase.javabean.PrintQueueMsg;
import com.alfredbase.store.sql.PrintQueueMsgSQL;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.MachineUtil;
import com.alfredbase.utils.NetUtil;
import com.birbit.android.jobqueue.CancelReason;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;

public class PrintJob extends Job  {
    private String printerIp;


    private int isLablePrinter;
    private int direction;
    private long localId;
    static final String TAG = "PrintJob";
    public static String reNext = "\n";
    public static final String localIPAddress = "127.0.0.1";

    protected int charSize = 33;  //tm81: 48, tm-u220:33, tm88:42
    //Print Data
    protected ArrayList<PrintData> data;

    protected ArrayList<PrintTscData> tdata;

    //Print QUEUE
    private String msgUUID;
    private String msgType;
    private int msgId; //all 0
    private Long created;
    private Long bizDate;
    private Boolean  isTian;
    // BluetoothSocket mmSocket;

    private static OutputStream outputStream = null;



    public PrintJob(Params params, String msgType, String uuid, Long bizDate) {

        super(params);
        this.data = new ArrayList<PrintData>();
        this.tdata = new ArrayList<PrintTscData>();
        this.msgType = msgType;
        this.msgUUID = uuid;
        this.msgId = msgId;
        this.bizDate = bizDate;
        this.created = System.currentTimeMillis();
    }

    public PrintJob(Params params, String msgType, String uuid, Long bizDate, Long created) {

        super(params);
       this.data = new ArrayList<PrintData>();

        this.msgType = msgType;
        this.msgUUID = uuid;
        this.msgId = msgId;
        this.bizDate = bizDate;
        this.created = created;
    }

//    public PrintJob(Params params) {
//
//        super(params);
//        this.tdata = new ArrayList<PrintTscData>();
//
//
//    }

//

    public String getPrinterIp() {
        return printerIp;
    }

    public void setPrinterIp(String printerIp) {
        this.printerIp = printerIp;

        Log.d(TAG, "setPrinterIp:" + this.printerIp);
    }


    public int getIsLablePrinter() {
        return isLablePrinter;
    }

    public void setIsLablePrinter(int isLablePrinter, int direction ,Boolean isTian) {
        this.isLablePrinter = isLablePrinter;
         this.direction=direction;
         this.isTian=isTian;
        Log.d(TAG, "setIsLablePrinter:" + this.isLablePrinter);
    }

    public void setCharSize(int size) {
        this.charSize = size;
    }
    public void setCharSize(String modifer) {
        if(!TextUtils.isEmpty(modifer) && modifer.toUpperCase().startsWith("TM-T88")){
            this.charSize = 42;
        }else{
            this.charSize = 48;
        }

    }


    // for Print Queue
    public PrintQueueMsg getJobForQueue() {
        PrintQueueMsg msg = new PrintQueueMsg();
        Gson gson = new Gson();

        msg.setPrinterIp(this.printerIp);
        msg.setCharSize(this.charSize);
        msg.setMsgUUID(this.msgUUID);
        msg.setMsgType(this.msgType);
        msg.setCreated(created);
        msg.setBizDate(bizDate);
        if(this.isLablePrinter==0) {
            msg.setData(gson.toJson(data));
        }else {
            msg.setData(gson.toJson(tdata));

        }

        return msg;
    }




    @Override
    public void onAdded() {
        LogUtil.e(this.getClass().getSimpleName(),"onRun------------------------------------");
        Log.d(TAG, "onAdded:" + this.printerIp);
        PrintQueueMsg content = PrintQueueMsgSQL.getUnsentMsgById(this.msgUUID, this.created);
        if (content != null) {
            this.printerIp = content.getPrinterIp().trim();


            PrintQueueMsgSQL.updatePrintQueueMsgStatus(ParamConst.PRINTQUEUE_MSG_QUEUED, this.msgUUID, this.created);
        }
    }

    @Override
    public void onRun() throws Throwable {
        boolean isPrintLink = false;
        boolean printed = false;
        boolean pingSuccess;
        Log.e(TAG, "onRun:" + this.printerIp);
        Log.e(TAG, "onRun:this uuid is " + this.msgUUID + ", then this object is" + this);
        LogUtil.e(this.getClass().getSimpleName(),"onRun------------------------------------");
        Gson gson = new Gson();
        PrintQueueMsg content = PrintQueueMsgSQL.getQueuedMsgById(this.msgUUID, this.created);
        if (content == null) {
            return;
        }




//		Subscriber<ESCPrinter> subscriber = new Subscriber<ESCPrinter>() {
//			@Override
//			public void onCompleted() {
//
//			}
//
//			@Override
//			public void onError(Throwable e) {
//
//			}
//
//			@Override
//			public void onNext(ESCPrinter escPrinter) {
//				if(escPrinter != null){
//
//				}
//			}
//
//		};
//
//		Observable.just(printerIp) // 输入类型 String
//				.map(new Func1<String, ESCPrinter>() {
//					@Override
//					public ESCPrinter call(String ip) { // 参数类型 String
//						return getESCPrinter(ip); // 返回类型 ESCPrinter
//					}
//				})
//				.subscribe(subscriber);

        if(getIsLablePrinter()==0) {
            this.data = gson.fromJson(content.getData(), new TypeToken<ArrayList<PrintData>>() {
            }.getType());
        }else {
            this.tdata = gson.fromJson(content.getData(), new TypeToken<ArrayList<PrintTscData>>() {
            }.getType());
        }

       Log.d("isPrintLink", this.isLablePrinter + "----订单json-" + content.getData().toString());
        ESCPrinter printer = PrintService.instance.getEscPrinterMap().get(this.printerIp);
        //ping printer first

        if(printerIp.contains(",")){
            if(getIsLablePrinter() == 1) {
                printer = new ESCPrinter(this.printerIp, isLablePrinter);
                printed =  printer.setUSBData(this.tdata, this.direction,this.isTian);
                if (printed) {
                    PrintQueueMsgSQL.updatePrintQueueMsgStatus(ParamConst.PRINTQUEUE_MSG_SUCCESS, this.msgUUID, this.created);
                    return;
                }
            }
        }
        if (printerIp.contains(":")) {

            if(getIsLablePrinter()==0) {
                if (printer == null) {
                    printer = new ESCPrinter(this.printerIp);
                    isPrintLink = printer.open();
                    PrintService.instance.putEscPrinterMap(this.printerIp, printer);
                } else {

                    if (!printer.isConnected()) {
                        isPrintLink = printer.reconnect();
                    } else {
                        isPrintLink = true;
                    }
                }
                if (isPrintLink) {

                    printed = printer.setData(this.data);
                }

            }else {

                    if (printer == null) {

                        printer = new ESCPrinter(this.printerIp,this.isLablePrinter);
                        isPrintLink = printer.open();
                        PrintService.instance.putEscPrinterMap(this.printerIp, printer);
                    } else {

                        if (!printer.isConnected()) {
                            isPrintLink = printer.reconnect();
                        } else {
                            isPrintLink = true;
                        }



                    if (isPrintLink) {
                        printed = printer.setTscData(this.tdata,this.direction,this.isTian);
                    }
                }


            }


            if (printed && isPrintLink) {
                PrintQueueMsgSQL.updatePrintQueueMsgStatus(ParamConst.PRINTQUEUE_MSG_SUCCESS, this.msgUUID, this.created);
            }
        } else {

          //  if(localIPAddress.equals(printerIp)) {
              //  pingSuccess = true;
           // }else {
                pingSuccess = NetUtil.ping(printerIp);
         //   }
            if (pingSuccess) {
                if (printer == null) {
                    printer = new ESCPrinter(this.printerIp);
                    isPrintLink = printer.open();
                    PrintService.instance.putEscPrinterMap(this.printerIp, printer);
                } else {
                    if (!printer.isConnected()) {
                        isPrintLink = printer.reconnect();
                    } else {
                        isPrintLink = true;
                    }
                }
                if (isPrintLink) {

                    printed = printer.setData(this.data);
                }

            } else {
                onCancel(CancelReason.CANCELLED_WHILE_RUNNING, null);
//			PrintQueueMsgSQL.updatePrintQueueMsgStatus(ParamConst.PRINTQUEUE_MSG_UN_SEND, this.msgUUID, this.created);
                if (printer != null)
                    printer.onSendFailed();

                Log.e(TAG, "onRun: this ip is failing ping, waiting next check PrintQueueMsg");
                return;
            }
            //	}
            if (printed && isPrintLink && pingSuccess) {
                PrintQueueMsgSQL.updatePrintQueueMsgStatus(ParamConst.PRINTQUEUE_MSG_SUCCESS, this.msgUUID, this.created);
            }
            if (!isPrintLink) {
                LogUtil.e("PrintJob","Printer unLink run next time");
                throw new RuntimeException("Printer unLink run next time");
            }
        }

        if (!printed) {
            LogUtil.e("PrintJob","Print Error");
            throw new RuntimeException("Print Error");
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        LogUtil.d(TAG, "onCancel:" + this.printerIp);
        PrintQueueMsgSQL.updatePrintQueueMsgStatus(ParamConst.SYNC_MSG_UN_SEND, this.msgUUID, this.created);
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.RETRY;
    }


    public static byte[] ByteTo_byte(Vector<Byte> vector) {
        int len = vector.size();
        byte[] data = new byte[len];

        for (int i = 0; i < len; ++i) {
            data[i] = ((Byte) vector.get(i)).byteValue();
        }

        return data;
    }

//	private ESCPrinter getESCPrinter(String ip){
//		boolean isPrintLink = NetUtil.ping(printerIp);
//		ESCPrinter printer = null;
//		if (isPrintLink) {
//			printer = PrintService.instance.getEscPrinterMap().get(ip);
//			if(printer == null){
//				printer = new ESCPrinter(this.printerIp);
//				PrintService.instance.putEscPrinterMap(this.printerIp, printer);
//			}else if (printer.isConnected()) {
//				//need reconnect
//				printer.reconnect();
//			}
//		}
//		return printer;
//	}


    protected void AddCut() {
        if(WifiCommunication.localIPAddress.equals(printerIp) && !MachineUtil.isHisense()){
            sunmiCut();
        }else{
            defaultCut();
        }
    }

    private void defaultCut(){
        PrintData cut = new PrintData();
        cut.setDataFormat(PrintData.FORMAT_CUT);
        this.data.add(cut);
    }

    private void sunmiCut(){
        PrintData cut = new PrintData();
        cut.setDataFormat(PrintData.FORMAT_FEED);
        cut.setMarginTop(2);
        this.data.add(cut);
    }

    protected void AddKickDrawer() {
        PrintData kick = new PrintData();
        kick.setDataFormat(PrintData.FORMAT_DRAWER);
        this.data.add(kick);
    }

    protected void addHortionalLine(int charSize) {
        String lstr = new String(new char[charSize]).replace('\0', '-').concat(reNext);
        PrintData line = new PrintData();
        line.setDataFormat(PrintData.FORMAT_TXT);
        line.setMarginTop(10);
        line.setText(lstr);
        this.data.add(line);
    }

    protected void addHortionaDoublelLine(int charSize) {
        String lstr = new String(new char[charSize]).replace('\0', '=').concat(reNext);
        PrintData line = new PrintData();
        line.setDataFormat(PrintData.FORMAT_TXT);
        line.setMarginTop(10);
        line.setTextBold(-1);
        line.setText(lstr);
        this.data.add(line);
    }

    protected void addBlankLine() {
        StringBuilder sbr = new StringBuilder();
        sbr.append(reNext);
        PrintData line = new PrintData();
        line.setDataFormat(PrintData.FORMAT_TXT);
        line.setMarginTop(20);
        line.setText(sbr.toString());
        this.data.add(line);
    }

    protected void AddSing() {
        PrintData sing = new PrintData();
        sing.setDataFormat(PrintData.FORMAT_SING);
        this.data.add(sing);
    }

    protected void addSingleLineCenteredText(int charSize, String txt, int height) {
        StringBuilder sbr = new StringBuilder();
        sbr.append(txt).append(reNext);
        PrintData line = new PrintData();
        line.setDataFormat(PrintData.FORMAT_TXT);
        line.setTextAlign(PrintData.ALIGN_CENTRE);
        line.setText(sbr.toString());
        line.setMarginTop(height);
        this.data.add(line);
    }

    protected void addSingleLineCenteredTextPaddingWithDash(int charSize, String txt, int height) {
        StringBuilder sbr = new StringBuilder();
        sbr.append(StringUtil.padCenterWithDash(txt, charSize)).append(reNext);
        PrintData line = new PrintData();
        line.setDataFormat(PrintData.FORMAT_TXT);
        line.setTextAlign(PrintData.ALIGN_CENTRE);
        line.setText(sbr.toString());
        line.setMarginTop(height);
        this.data.add(line);
    }

    protected void addSingleLineText(int charSize, String txt, int height) {
        StringBuilder sbr = new StringBuilder();
        sbr.append(StringUtil.padLeft(txt, charSize)).append(reNext);
        PrintData line = new PrintData();
        line.setDataFormat(PrintData.FORMAT_TXT);
        line.setText(sbr.toString());
        line.setMarginTop(height);
        this.data.add(line);
    }

    public ArrayList<PrintData> getData() {
        return data;
    }

    public void setData(ArrayList<PrintData> data) {
        this.data = data;
    }





}
