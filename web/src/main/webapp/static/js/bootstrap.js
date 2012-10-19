define([
  'dojo/ready',
  'dojo/query',
  'dojo/dom-class'
], function(
  ready,
  query,
  domClass
) {
  
  ready(function() {
    
    query('.control-group').forEach(function(group) {
      query('.lift_error', group).forEach(function(elem) {
        domClass.add(elem, 'help-inline');
        domClass.add(group, 'error');
      });
    });
    
  });
  
});