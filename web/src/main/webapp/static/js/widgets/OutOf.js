define([
  'dojo/_base/lang',
  'dojo/_base/declare',
  'dijit/_Widget',
  'dijit/_TemplatedMixin',
], function() {
  
  return declare([_Widget, _TemplatedMixin], {
    
    templateString: "<span></span>",
    
    // array of items
    items: undefined,
    
    // function if item matches
    matches: undefined,
    
    postCreate: function() {
        this._update();
    },
    
    _update: function() {
      var count = _.filter(this.items, this.matches).length;
      show(count, this.items.length);
    },
    
    _show: function(count, total) {
      this.domNode.innerHTML = count + " of " + total;
    }
    
  });
  
});