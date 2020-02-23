package com.alfred.remote.printservice;

public interface WIFIPrintCallback {
   public void onConnected();
   public void onDisconnected();
   public void onSendFailed();
}
