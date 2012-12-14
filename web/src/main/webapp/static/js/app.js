require(/*{
  baseUrl:"",
  packages: [
    { name: 'dojo', location: '/static/vendor/dojo/1.8.1/dojo' },
    { name: 'dijit', location: '/static/vendor/dojo/1.8.1/dijit' },
    { name: 'dojox', location: '/static/vendor/dojo/1.8.1/dojox' },
    { name: "put-selector", location: '/static/vendor/put-selector' },
    { name: "xstyle", location: '/static/vendor/xstyle' },
    { name: 'iconhub', location: '/static/js'}
  ],
  cache:{}
},*/[
  /*
   'dbootstrap',
   */
  'dojo/parser',
  'dojo/ready',
  'dojo/query',
  'iconhub/menubar',
  'iconhub/bootstrap',
  'iconhub/test'
], function(
  parser,
  ready,
  query,
  menubar,
  bootstrap,
  test)
{
  ready(function() {
    query('body > .container').forEach(function(n) { n.style.display = 'block'; });
    parser.parse();
  });
});