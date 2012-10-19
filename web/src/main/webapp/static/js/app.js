require({
  baseUrl:"",
  packages: [
    /*
    "dojo,dijit,dojox,dgrid,put-selector,xstyle,dbootstrap".split(","),
    */
    
    { name: "put-selector", location: '/static/vendor/put-selector' },
    { name: "xstyle", location: '/static/vendor/xstyle' },
    /*
    { name: "dbootstrap", location: '/static/vendor/dbootstrap' },
    */
    { name: 'iconhub', location: '/static/js'}
  ],
  cache:{}
},[
  /*
   'dbootstrap',
   */
  'dojo/parser',
  'dojo/ready',
  'dojo/query',
  'iconhub/menubar',
  'iconhub/bootstrap'
], function(
  parser,
  ready,
  query,
  menubar,
  bootstrap)
{
  ready(function() {
    query('body > .container').forEach(function(n) { n.style.display = 'block'; });
    parser.parse();
  });
});