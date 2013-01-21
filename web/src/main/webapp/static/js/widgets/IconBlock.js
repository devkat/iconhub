define([
  'dojo/_base/lang',
  'dojo/_base/declare',
  'dijit/_Widget',
  'dijit/_TemplatedMixin',
  'dojo/on',
  "dojo/_base/fx",
  "dojo/dom-style",
  "dojo/text!./templates/IconBlock.html"
], function(
  lang,
  declare,
  _Widget,
  _TemplatedMixin,
  on,
  fx,
  style,
  template
) {
  
  return declare([_Widget, _TemplatedMixin], {
    templateString: template,
    title: undefined,
    url: undefined,
    load: undefined,
    error: undefined,
    loaded: false,
    size: undefined,
    
    postCreate: function() {
      this.titleNode.innerHTML = this.title;
      var pathSegments = this.url.split('/');
      this.filenameNode.innerHTML = pathSegments[pathSegments.length - 1];
      on(this.dummyImageNode, 'load', lang.hitch(this, function(evt) {
        this.loaded = true;
        var img = this.dummyImageNode;
        this.size = Math.max(img.width, img.height);
        this.dimensionsNode.innerHTML = img.width + " &times; " + img.height;
        if (this.load !== undefined) {
          this.load();
        }
      }));
      this.dummyImageNode.src = this.url;
      this.imageNode.setAttribute("alt", this.title);
      this.imageNode.src = this.url;
    },
    
    show: function() {
      var node = this.domNode;
      if (style.get(node, "display") === "none") {
        style.set(node, "opacity", "0");
        style.set(node, "display", "");
        fx.fadeIn({
          node: node,
          duration: 500,
        }).play();
      }
    },
    
    hide: function() {
      var node = this.domNode;
      if (style.get(node, "display") !== "none") {
        fx.fadeOut({
          node: node,
          duration: 500,
          onEnd: function() {
            style.set(node, "display", "none");
          }
        }).play();
      }
    }
    
  });
  
});