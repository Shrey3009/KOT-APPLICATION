
	var devices =[{"category":"厨房显示设备", "devicetype":"KDS", "devices":[]},
	          		{"category":"打印机","devicetype":"Printer", "devices":[]},
	          		{"category":"点菜宝","devicetype":"Waiter","devices":[]}];
	              	              
    var KDS_Devices = [];
    var Waiter_Devices = [];
    var Printer_Devices = [];
    var POSInfo = {};
    
	function updateDeviceInMenu() {
	   var tmplfunc = _.template($('#leftcol_tmpl').html());
	   $('#sidemenucontaier').html(tmplfunc({'locations':devices}));
	}
	
	
	/* slide menu callback*/
	function menuClick(evt, hassub) {
	  var curtg = $(evt.currentTarget);
      var devtype = curtg.attr('data').toLowerCase();
      SELECTED_LEVEL_1 = devtype;
      if (devtype == "printer"){
         loadAllPrinters();
      }else if(devtype == "kds") {
    	 loadAllKDS();
      }else if (devtype == "waiter") {
    	 loadWaiterTablets();
      }
      if (hassub) {
        $($('#sidemenucontaier .mp-level-open .level2')[0]).addClass('active');
        SELECTED_LEVEL_2 = $($('#sidemenucontaier .mp-level-open .level2')[0]).html();
      }
	}
	
	function levelClick(evt,level){
	  if (level==2) {
	      $('.level2').removeClass('active');
	      $(evt.target).addClass("active");
	      SELECTED_LEVEL_2 = $(evt.target).html();
	      if ($(evt.target).attr("data") == "KDS") {
	         filterKDS($(evt.target).html());
	      }else if ($(evt.target).attr("data")=="Printer") {
	         filterPrinters($(evt.target).html());
	         
	      }
      }else if (level==1) {
         SELECTED_LEVEL_1 = $(evt.target).attr('data').toLowerCase();
         if (SELECTED_LEVEL_1 == "waiter")
    	    loadWaiterTablets();
      }
	
	}
	
	function backClick(evt){
	  sideMenuBack();
	}
	
	function updateSideMenu(menudata) {
	   var params = $.parseJSON(decodeURIComponent(menudata).replace(new RegExp("\\+","gm")," "));
	   var printerlist = params['printers'];
	   var kdslist = params['kds'];
	   POSInfo = params['mainPosInfo'];
	   
	   $.each(devices, function( idx, devObj ) {
	     if (devObj.devicetype == "Printer")
	        devObj.devices = printerlist;
	     if (devObj.devicetype == "KDS")
	        devObj.devices = kdslist;
	   });
	   
	   updateDeviceInMenu();
       var a = new mlPushMenu( document.getElementById( 'mp-menu' ), null, 
       							{"menuClick": menuClick,
       							 "levelClick":levelClick,
       							 "backClick":backClick} );
       a._openMenu();
       showPosInfo();
       generateDeviceQR($('.ipaddr').html());
       
	}
    
    //call android function
    function loadAllPrinters() {
       var param = {"js_callback":"RefreshPrinters"};
       window.JavaConnectJS.send("LoadPrinters",JSON.stringify(param));
    }
    
    function _refreshPrinter(printers){
	   var allprinters = $.parseJSON(decodeURIComponent(printers).replace(new RegExp("\\+","gm")," "));
	   Printer_Devices = allprinters;
	   if (SELECTED_LEVEL_1!=null && SELECTED_LEVEL_1.toLowerCase() =="printer") {
	     if (SELECTED_LEVEL_2==null) {
	   	     var tmplfunc = _.template($('#printer_tmpl').html());
	   	     $('.deviceinfo').html(tmplfunc({'printers':allprinters}));	     
	     }else {
		  	 filterPrinters(SELECTED_LEVEL_2);		       
	     }
	   }
    }
    function _refreshAllKDS(kds){
	   var allkds = $.parseJSON(decodeURIComponent(kds).replace(new RegExp("\\+","gm")," "));
	   KDS_Devices = allkds;  
   
	   if (SELECTED_LEVEL_1!=null && SELECTED_LEVEL_1.toLowerCase()=="kds") {
	     if (SELECTED_LEVEL_2==null) {
	   		var tmplfunc = _.template($('#kds_tmpl').html());
	   	    $('.deviceinfo').html(tmplfunc({'kdsdevices':allkds}));	     
	     }else {
		  	 filterKDS(SELECTED_LEVEL_2);		       
	     }
	  } 	   
    }
        
    function loadPrintersByLocationName () {
       return null;
    }
    
    function loadAllKDS() {
       var param = {"js_callback":"RefreshKdsDevices"};
       window.JavaConnectJS.send("LoadKdsDevices",JSON.stringify(param));
    }

    function loadWaiterTablets(param) {
       var param = {"js_callback":"RefreshWaiterDevices"};
       window.JavaConnectJS.send("LoadWaiterDevices",JSON.stringify(param));
    }
    
    function _refreshWaiterTablets(param) {
	   var waiterTablets = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
	   Waiter_Devices =  waiterTablets;   
	   if (SELECTED_LEVEL_1 !=null || SELECTED_LEVEL_1 =="Waiter") {
	   	  var tmplfunc = _.template($('#tablets_tmpl').html());
	   	  $('.deviceinfo').html(tmplfunc({'tablets':waiterTablets}));  
	   }  
    }
    
    function sideMenuBack() {
      $('.deviceinfo').html("");
      $('.level2').removeClass('active');
      SELECTED_LEVEL_1 = null;
      SELECTED_LEVEL_2 = null;      
    }
    
    function filterKDS(name) {
       var allkds = [];
 	   $.each(KDS_Devices, function( idx, kdsObj ) {
	     if (kdsObj.name ===name )
	        allkds.push(kdsObj);
	   });
	         
	   var tmplfunc = _.template($('#kds_tmpl').html());
	   $('.deviceinfo').html(tmplfunc({'kdsdevices':allkds}));       
    }
    function filterPrinters(name) {
       var allprinters = [];
 	   $.each(Printer_Devices, function( idx, wtObj ) {
	     if (wtObj.name===name && wtObj.device_id > 0 )
	        allprinters.push(wtObj);
	   });
	   if (allprinters.length ==0) {
	 	   $.each(Printer_Devices, function( idx, wtObj ) {
		     if ( wtObj.device_id < 0 )
		        allprinters.push(wtObj);
		   });	   
	   }
	             
	   var tmplfunc = _.template($('#printer_tmpl').html());
	   $('.deviceinfo').html(tmplfunc({'printers':allprinters}));
	   if (allprinters.length ==0) 
	       _addPrinterManuAddBtn();	         
    }    
    function _newKdsDeviceAdded(newKds) {
      var kds = $.parseJSON(decodeURIComponent(newKds).replace(new RegExp("\\+","gm")," "));
      var found = false;
  	  $.each(KDS_Devices, function( idx, kdsObj ) {
	     if (kdsObj.mac ===kds.mac) {
	        kdsObj.IP = kds.IP;
	        kdsObj.name = kds.name;
	        found = true;
	     }
	  }); 
	  if (!found)
	     KDS_Devices.push(kds); 

	  if (SELECTED_LEVEL_1!=null && SELECTED_LEVEL_1=="kds") {
	     if (SELECTED_LEVEL_2==null) {
		  	 var tmplfunc = _.template($('#kds_tmpl').html());
		  	 $('.deviceinfo').html(tmplfunc({'kdsdevices':KDS_Devices}));	     
	     }else if (SELECTED_LEVEL_2 == kds.name) {
		  	 filterKDS(kds.name);		       
	     }
	  }      
    }
    
    function _newWaiterDeviceAdded(newWaiter) {
      var waiter = $.parseJSON(decodeURIComponent(newWaiter).replace(new RegExp("\\+","gm")," "));
      var found = false;
  	  $.each(Waiter_Devices, function( idx, wtObj ) {
	     if (wtObj.waiterId ===waiter.waiterId) {
	        wtObj.ip = waiter.ip;
	        wtObj.mac = waiter.mac;
	        found = true;
	     }
	  }); 
	  if (!found)
	     Waiter_Devices.push(waiter);   
	  
	  var tmplfunc = _.template($('#tablets_tmpl').html());
	  $('.deviceinfo').html(tmplfunc({'tablets':Waiter_Devices}));     
    }
    
    function showPosInfo() {
	    var tmplfunc = _.template($('#mainposinfo_tmpl').html());
		$('.qripdata').html(tmplfunc({'mainPosInfo':POSInfo}));    
    
    }
    function generateDeviceQR(ip) {
		var options = {
				render: 'canvas',
				ecLevel: 'H',
				minVersion: 5,
				fill: "#333333",
				background: "#ffffff",
				text: ip,
				size: 200,
				radius: 0.5,
				quiet: 2,
				mode: 2,
				mSize: 0.11,
				mPosX: 0.5,
				mPosY: 0.5,
				label: "Alfred"
			};

		$("#deviceqrimg").empty().qrcode(options);
    } 
    
    function goback(evt) {
            evt.stopImmediatePropagation();            
            window.JavaConnectJS.send("SystemBack",null);
    }
    
    //call android to load Tables data
    function _loadDevices(){
         var param = {"js_callback":"RefreshDevices"};
         window.JavaConnectJS.send("LoadDevices",JSON.stringify(param));
    }  
    
    
    function _JsConnectAndroid(funcname, param) {
       if (funcname == "RefreshDevices") {
          updateSideMenu(param); 
       }else if(funcname =="RefreshPrinters"){
         _refreshPrinter(param);
       }else if(funcname == "RefreshWaiterDevices"){
         _refreshWaiterTablets(param);
       }else if(funcname == "RefreshKdsDevices") { 
         _refreshAllKDS(param);
       }else if (funcname =="NewKdsDeviceAdded") {
         _newKdsDeviceAdded(param);
       }else if (funcname =="NewWaiterDeviceAdded") {
         _newWaiterDeviceAdded(param);
       }
    }
    
    function assignPrinter(evt) {
      var printname = $(evt.currentTarget).parent().find('.prtname').html();
      var printip = $(evt.currentTarget).parent().find('.prtip').html();
      var param = {"printerName":printname,
                   'printerIp':printip, 
                   'assignTo':SELECTED_LEVEL_2,
                   "js_callback":"RefreshPrinters"};
      window.JavaConnectJS.send("AssignPrinterDevice",JSON.stringify(param));      
    }
    function _addPrinterManuAddBtn() {
      $('.deviceinfo').prepend($('#add_printer_tmpl').html());
    }
    function unassignPrinter(evt) {
      var printname = $(evt.currentTarget).parent().find('.prtname').html();
      var printip = $(evt.currentTarget).parent().find('.prtip').html();
      var deviceId = $(evt.currentTarget).attr("data");
      var param = {"printerName":printname,
                   'printerIp':printip, 
                   'deviceId':deviceId,
                   'assignTo':SELECTED_LEVEL_2,
                   "js_callback":"RefreshPrinters"};
      window.JavaConnectJS.send("UnassignPrinterDevice",JSON.stringify(param));      
    }
    function addPrinterDevice(evt) {
      var param = {'assignTo':SELECTED_LEVEL_2,
                   "js_callback":"RefreshPrinters"};
      window.JavaConnectJS.send("Add_printer_device",JSON.stringify(param));      
    }    
    var SELECTED_LEVEL_1 = null;
    var SELECTED_LEVEL_2 = null;
    
    $(document).ready(function () {
		
		//expose interface to Android
		window.JsConnectAndroid = _JsConnectAndroid;   

        FastClick.attach(document.body);
        
    	_loadDevices();

        $('#backBtn').on('click touched', goback);
        $('.deviceinfo').on('click touched','.assignBtn',assignPrinter);
        $('.deviceinfo').on('click touched','.unassignBtn',unassignPrinter);
        $('.deviceinfo').on('click touched','#addPrinterBtn', addPrinterDevice);
    });
    