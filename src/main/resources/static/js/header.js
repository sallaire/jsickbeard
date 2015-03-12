$(document).ready(function(){ 
    	
        $("ul.sf-menu").supersubs({ 
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
        $("#MainMenu.sf-menu ul li a").mouseover(function(){
            imgsrc = $(this).children("img").attr("src");
            if(imgsrc!=null) {
                matches = imgsrc.match(/_over/);
                if (!matches) {
                    imgsrcON = imgsrc.replace(/.png$/ig,"_over.png");
                    $(this).children("img").attr("src", imgsrcON);
                }
                $("#MainMenu.sf-menu ul li a").mouseout(function(){
                    $(this).children("img").attr("src", imgsrc);
                });
            }
        });
        $("#MainMenu.sf-menu ul li img").each(function() {
            rollsrc = $(this).attr("src");
            rollON = rollsrc.replace(/.png$/ig,"_over.png");
            $("<img>").attr("src", rollON);
        });

        $("#NAVhome").addClass("current");

        $("a.confirm").bind("click",function(e) {
            $('#MainMenu.sf-menu').hideSuperfishUl();
            e.preventDefault();
            var target = $( this ).attr('href');
            if ( confirm("Are you sure you want to " + $(this).text() + "?") )
                location.href = target;
            return false;
        });

    });