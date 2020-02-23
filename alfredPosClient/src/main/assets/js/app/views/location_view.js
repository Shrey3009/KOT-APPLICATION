var app = app || {};

(function ($) {
    //Generate html from Template
    app.locationItemView = Backbone.View.extend({
        tagName: 'li',
        el: '#locationList',
        template: _.template($('#locationItem-template').html()),
        initialize: function () {
            this.listenTo(this.model, 'change', this.render);
        },
        render: function () {
            this.$el.append(this.template(this.model.toJSON()));
            return this;
        }
    });
    //LocationList Column
    app.locationlistView = Backbone.View.extend({
        el: '#locationApp',
        events: {'click .rowright': 'showTables'},
        initialize: function () {
            this.listenTo(app.locationColection, 'add', this.addOne);
            //this.listenTo(app.locationColection, 'reset', this.addAll);
            //this.listenTo(app.locationColection, 'all', this.render);
        },
        showTables: function (event) {
            var data;
            if (event == undefined) {
                data = app.locationColection.at(0).get("id");
            } else {
                data = $(event.currentTarget).attr('data');
            }
            $(".selected").removeClass("selected");
            var parent;
            if (event == undefined) {
              parent = $("#location_" + data).parent();
            }else{
              parent = $(event.currentTarget).parent();
            }

            parent.addClass("selected");
            app.tableCollection = new app.TaCollection();
            app.locationColection.each(function (num, index) {
                if (num.get('id') == data) {
                    //get all tablets
                    app.tableCollection.add(app.locationColection.at(index).get('tables'));
                }
            });
            app.tablesView = new app.tablelistView({collection: app.tableCollection});
            app.tablesView.render();
        },
        
        render: function () {
            this.addAll();
            this.showTables(event);
        },
        
        addOne: function (loc) {
            var view = new app.locationItemView({ model: loc });
            $('#locationList').append(view.render().el);
        },
        
        addAll: function () {
            this.$('#locationList').html('');
            app.locationColection.each(this.addOne, this);
        }
    });

	function getTableData(tableList,locid) {
	  var result=[];
	  $.each(tableList, function( idx, tabObj ) {
	     if (tabObj.placesId == locid)
	        result.push(tabObj);
	  });
	  return result;
	}
	
	function getTableStatus(tableStatusList,locid) {
	  var result=null;
	  $.each(tableStatusList, function( idx, tabStuObj ) {
	     if (tabStuObj.id == locid)
	        result=tabStuObj;
	  });
	  return result;
	}
		    
    function parseLocationTableJson(ptparams){
    
		var placeList = ptparams['places'];
		var tableList = ptparams['tables'];
		var tableStatusList = ptparams['tableStatusInfo'];
		
		var locationTableJson = []
		
		for (var i=0; i<placeList.length; i++) {
		   var placeobj = placeList[i];
		   placeobj.tables = getTableData(tableList, placeobj.id)
		   locationTableJson.push(placeobj);
		   var status = getTableStatus(tableStatusList,placeobj.id);
		   placeobj['pending'] = status['diningNum'];
		   placeobj['billing'] = status['inCheckoutNum'];    
		}
        return locationTableJson;
    }
    
    function backMainIndex(evt){
            evt.stopImmediatePropagation();            
            window.JavaConnectJS.send("SystemBack",null);
    }
                
    //refresh tables
    function _refreshTables(tables) {
       var data = $.parseJSON(decodeURIComponent(tables).replace(new RegExp("\\+","gm")," "));
       var alldata = parseLocationTableJson(data);
       app.locationColection.reset();
       app.locationColection.add(alldata);
       app.locationView.render();
    }
    
    //refresh table status
    function _refreshTableStatus(locobj) {
        var data = $.parseJSON(decodeURIComponent(locobj).replace(new RegExp("\\+","gm")," "));
	    var tables = data.tables;
	    var locationId = data.id;
	    var refreshIndex = 0;
	    app.locationColection.each(function (num, index) {
	        if (num.get('id') == locationId) {
	            refreshIndex = index;
	        }
	    });
	    _.each(tables, function (table) {
	        _.find(app.locationColection.at(refreshIndex).get('tables'), function (num, i) {
	            if (table.id == app.locationColection.at(refreshIndex).get('tables')[i].id) {
	                app.locationColection.at(refreshIndex).get('tables')[i].tableStatus = table.tableStatus
	            }
	        });
	    });
	    app.tablesView.changeMoudel(tables);        
    }
    
    //call android to load Tables data
    function _loadLocationAndTables(){
           var param = {"js_callback":"RefreshTables"};
           window.JavaConnectJS.send("LoadTables",param);
    }    
    //exposed for android
    function JsConnectAndroid(funcname, param) {
       if (funcname == "RefreshTables") {
          _refreshTables(param); 
       }
       else if (funcname == "RefreshTableStatus") {
         _refreshTableStatus(param); 
       }    
    }

    app.locationView = new app.locationlistView();
    document.addEventListener('touchmove', function (e) {
        e.preventDefault();
    }, false);
		var scroller = new FTScroller(document.getElementById('tableApp'), {
			scrollingX: false
		});        
    FastClick.attach(document.body);
    //expose interface to Android
    window.JsConnectAndroid = JsConnectAndroid; 
    $('#backBtn').bind('click',backMainIndex);
    $('#refBut').bind('click', _loadLocationAndTables);
         
    _loadLocationAndTables();
})(jQuery);