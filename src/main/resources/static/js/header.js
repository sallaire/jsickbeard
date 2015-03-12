function initActions() {
        \$("#SubMenu a[href^='$sbRoot/home/restart/']").addClass('btn').html('<span class="ui-icon ui-icon-refresh pull-left"></span> Restart </a>');
        \$("#SubMenu a[href^='$sbRoot/manage/manageSearches/forceVersionCheck']").addClass('btn').html('<span class="ui-icon ui-icon-search pull-left"></span> Update </a>');
       	\$("#SubMenu a[href^='$sbRoot/home/shutdown/']").addClass('btn').html('<span class="ui-icon ui-icon-power pull-left"></span> Shutdown </a>');		
		\$("#SubMenu a:contains('Edit')").addClass('btn').html('<span class="ui-icon ui-icon-pencil pull-left"></span> Edit </a>');
		\$("#SubMenu a:contains('Delete')").addClass('btn').html('<span class="ui-icon ui-icon-trash pull-left"></span> Delete </a>');
		\$("#SubMenu a:contains('Clear History')").addClass('btn confirm').html('<span class="ui-icon ui-icon-trash pull-left"></span> Clear History </a>');
		\$("#SubMenu a:contains('Trim History')").addClass('btn confirm').html('<span class="ui-icon ui-icon-trash pull-left"></span> Trim History </a>');
		\$("#SubMenu a:contains('Trunc Episode Links')").addClass('btn confirm').html('<span class="ui-icon ui-icon-trash pull-left"></span> Trunc Episode Links </a>');
		\$("#SubMenu a:contains('Trunc Episode List Processed')").addClass('btn confirm').html('<span class="ui-icon ui-icon-trash pull-left"></span> Trunc Episode List Processed </a>');
        \$("#SubMenu a[href='$sbRoot/errorlogs/clearerrors']").addClass('btn').html('<span class="ui-icon ui-icon-trash pull-left"></span> Clear Errors </a>');
		\$("#SubMenu a:contains('Re-scan')").addClass('btn').html('<span class="ui-icon ui-icon-refresh pull-left"></span> Re-scan </a>');
		\$("#SubMenu a:contains('Backlog Overview')").addClass('btn').html('<span class="ui-icon ui-icon-refresh pull-left"></span> Backlog Overview </a>');
        \$("#SubMenu a[href='$sbRoot/home/updatePLEX/']").addClass('btn').html('<span class="ui-icon ui-icon-refresh pull-left"></span> Update PLEX </a>');
		\$("#SubMenu a:contains('Force')").addClass('btn').html('<span class="ui-icon ui-icon-transfer-e-w pull-left"></span> Force Full Update </a>');
		\$("#SubMenu a:contains('Rename')").addClass('btn').html('<span class="ui-icon ui-icon-tag pull-left"></span> Preview Rename </a>');
		\$("#SubMenu a:contains('French')").addClass('btn').html('<span class="ui-icon ui-icon-refresh pull-left"></span> French Search </a>');
		\$("#SubMenu a[href='$sbRoot/config/subtitles/']").addClass('btn').html('<span class="ui-icon ui-icon-comment pull-left"></span> Search Subtitles </a>');
		\$("#SubMenu a[href^='$sbRoot/home/subtitleShow']").addClass('btn').html('<span class="ui-icon ui-icon-comment pull-left"></span> Download Subtitles </a>');
		\$("#SubMenu a[href^='$sbRoot/home/subtitleShowClean']").addClass('btn').html('<span class="ui-icon ui-icon-comment pull-left"></span> Clean Subtitles </a>');
		\$("#SubMenu a:contains('Settings')").addClass('btn').html('<span class="ui-icon ui-icon-search pull-left"></span> Search Settings </a>');
		\$("#SubMenu a:contains('Provider')").addClass('btn').html('<span class="ui-icon ui-icon-search pull-left"></span> Search Providers </a>');
		\$("#SubMenu a:contains('General')").addClass('btn').html('<span class="ui-icon ui-icon-gear pull-left"></span> General </a>');
		\$("#SubMenu a:contains('Episode Status')").addClass('btn').html('<span class="ui-icon ui-icon-transferthick-e-w pull-left"></span> Episode Status Management </a>');
		\$("#SubMenu a:contains('Missed Subtitle')").addClass('btn').html('<span class="ui-icon ui-icon-transferthick-e-w pull-left"></span> Missed Subtitles </a>');
		\$("#SubMenu a[href='$sbRoot/home/addShows/']").addClass('btn').html('<span class="ui-icon ui-icon-video pull-left"></span> Add Show </a>');
		\$("#SubMenu a:contains('Processing')").addClass('btn').html('<span class="ui-icon ui-icon-folder-open pull-left"></span> Post-Processing </a>');
		\$("#SubMenu a:contains('Manage Searches')").addClass('btn').html('<span class="ui-icon ui-icon-search pull-left"></span> Manage Searches </a>');
		\$("#SubMenu a:contains('Notification')").addClass('btn').html('<span class="ui-icon ui-icon-note pull-left"></span> Notification </a>');
		\$("#SubMenu a:contains('Update show in XBMC')").addClass('btn').html('<span class="ui-icon ui-icon-refresh pull-left"></span> Update show in XBMC </a>');
        \$("#SubMenu a[href='$sbRoot/home/updateXBMC/']").addClass('btn').html('<span class="ui-icon ui-icon-refresh pull-left"></span> Update XBMC </a>');
		
	}
    \$(document).ready(function(){ 
    	
    	initActions();
        \$("ul.sf-menu").supersubs({ 
            minWidth:    12,   // minimum width of sub-menus in em units 
            maxWidth:    27,   // maximum width of sub-menus in em units 
            extraWidth:  1     // extra width can ensure lines don't sometimes turn over 
                               // due to slight rounding differences and font-family 
        }).superfish({
            delay:         150,
            disableHI:     true, 
            animation:   {opacity:'show',height:'show'},
            speed:       'fast',
            dropShadows: false
        });
        \$("#MainMenu.sf-menu ul li a").mouseover(function(){
            imgsrc = \$(this).children("img").attr("src");
            if(imgsrc!=null) {
                matches = imgsrc.match(/_over/);
                if (!matches) {
                    imgsrcON = imgsrc.replace(/.png$/ig,"_over.png");
                    \$(this).children("img").attr("src", imgsrcON);
                }
                \$("#MainMenu.sf-menu ul li a").mouseout(function(){
                    \$(this).children("img").attr("src", imgsrc);
                });
            }
        });
        \$("#MainMenu.sf-menu ul li img").each(function() {
            rollsrc = \$(this).attr("src");
            rollON = rollsrc.replace(/.png$/ig,"_over.png");
            \$("<img>").attr("src", rollON);
        });

        \$("#NAV$topmenu").addClass("current");

        \$("a.confirm").bind("click",function(e) {
            \$('#MainMenu.sf-menu').hideSuperfishUl();
            e.preventDefault();
            var target = \$( this ).attr('href');
            if ( confirm("Are you sure you want to " + \$(this).text() + "?") )
                location.href = target;
            return false;
        });

    });