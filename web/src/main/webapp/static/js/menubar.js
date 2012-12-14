define([
  'dojo/_base/lang',
  'dojo/ready',
  'dojo/query',
  'dojo/dom-attr',
  'dojo/dom-construct',
  'dojo/dom-class',
  "dijit/MenuBar",
  "dijit/MenuBarItem",
  "dijit/DropDownMenu",
  "dijit/MenuItem",
  "dijit/MenuSeparator",
  "dijit/PopupMenuItem",
  "dijit/PopupMenuBarItem"

], function(
  lang,
  ready,
  query,
  domAttr,
  domConstruct,
  domClass,
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
          dropDown = null,
          props = getLinkProps(li);
          
        query('ul', li).forEach(function(ul) {
          dropDown = new DropDownMenu();
          query('li', ul).forEach(function(li) {
            var props = getLinkProps(li);
            dropDown.addChild(new MenuItem(props));
          });
        });

        if (li.id === 'selectedMenu') {
          props.selected = true;
        }
        var menu = dropDown ? new PopupMenuBarItem(lang.mixin(props, { popup: dropDown})) : new MenuBarItem(props);
        /*
        if (submenu) {
          domClass.add(menu, 'dropdown');
        }
        */
        
        menuBar.addChild(menu);
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