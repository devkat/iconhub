define([
  'dojo/_base/lang',
  'dojo/_base/fx',
  'dojo/ready',
  'dojo/query',
  'dojo/dom-attr',
  'dojo/dom-construct',
  'dojo/dom-class',
  'dojo/on',
  "dijit/MenuBar",
  "dijit/MenuBarItem",
  "dijit/DropDownMenu",
  "dijit/MenuItem",
  "dijit/MenuSeparator",
  "dijit/PopupMenuItem",
  "dijit/PopupMenuBarItem"

], function(
  lang,
  fx,
  ready,
  query,
  domAttr,
  domConstruct,
  domClass,
  on,
  MenuBar,
  MenuBarItem,
  DropDownMenu,
  MenuItem,
  MenuSeparator,
  PopupMenuItem,
  PopupMenuBarItem
) {

  ready(function() {

    var name2icon = {
      login: 'signin',
      my_account: 'user',
      my_iconhub: 'home',
      explore: 'eye-open'
    };

    query('#iconhub-menu').forEach(function(menuBarNode) {
      query('> li', menuBarNode).forEach(function(li) {

        _.each(li.className.split(/\s/), function(cl) {
          if (cl.startsWith('menu-item-')) {
            var iconName = name2icon[cl.substring('menu-item-'.length)];
            if (iconName) {
              var icon = domConstruct.create('i', {'class' : 'icon-' + iconName + ' icon-large'});
              domConstruct.place(icon, li, 'first');
            }
          }
        });
        
        /*
        if (props.selected) {
          query('ul', li).forEach(function(ul) {
            subMenuBar = new MenuBar();
            domClass.add(subMenuBar, 'nav nav-pills');
            query('li', ul).forEach(function(li) {
              var item = new MenuBarItem(lang.mixin(getLinkProps(li), {
                selected: li.id === 'selectedMenu'
              }));
              subMenuBar.addChild(item);
            });
            query('#iconhub-submenu-row div').forEach(function(n) {
              domConstruct.place(subMenuBar.domNode, n);
            });
          });
        }
        else {
          on(menuItem.domNode, 'mouseenter', function(evt) {
            fx.anim(icon, { "fontSize": { start: 10, end:13, units:"pt" }}, 200);
          });
          on(menuItem.domNode, 'mouseleave', function(evt) {
            fx.anim(icon, { "fontSize": { start: 13, end:10, units:"pt" }}, 200);
          });
        }
        
        menuBar.addChild(menuItem);
        */
      });

      
      /*
      query('a', menubar).forEach(function(a) {
        domAttr.set(a.parentNode, 'onclick', "window.location.href='" + a.href + "'");
        domConstruct.create('span', {innerHTML: a.innerHTML}, a, 'before');
        a.parentNode.removeChild(a);
      });
      
      var i = 0;
      query('ul', menubar).forEach(function(ul) {
        domAttr.set(ul, 'data-dojo-type', 'dijit/DropDownMenu');
        domAttr.set(ul, 'id', 'menu' + i++);
      });
      query('ul li', menubar).forEach(function(li) {
        domAttr.set(li, 'data-dojo-type', 'dijit/MenuItem');
      });
       */
      //domConstruct.place(menuBar.domNode, menuBarNode, 'replace');
    });
  });
});