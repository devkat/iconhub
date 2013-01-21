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
    query('#iconhub-menu').forEach(function(menuBarNode) {
      var menuBar = new MenuBar();
      domClass.add(menuBar, 'nav nav-pills');
      
      function getLinkProps(node) {
        var info = {};
        query('> a, > span', node).forEach(function(n) {
          info = { label: n.innerHTML };
          if (n.href) {
            info.onClick = "window.location.href='" + n.href + "'";
          }
        });
        return info;;
      }

      query('> li', menuBarNode).forEach(function(li) {
        var
          props = getLinkProps(li);
          
        if (li.id === 'selectedMenu' || query('li[id="selectedMenu"]', li).length > 0) {
          props.selected = true;
        }

        var menuItem = new MenuBarItem(props);
        var icon = domConstruct.create('i', {'class' : 'icon-camera-retro icon-large'});
        domConstruct.place(icon, menuItem.domNode, 'first');
        
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
      domConstruct.place(menuBar.domNode, menuBarNode, 'replace');
    });
  });
});