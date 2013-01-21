define([
  'dojo/_base/lang',
  'dojo/_base/xhr',
  'dojo/on',
  'dojo/dom',
  'dojo/dom-construct',
  'dojo/dom-class',
  "dojo/_base/array",
  "dojox/form/RangeSlider",
  "dojo/dom-style",
  "dojo/_base/event",
  "dojo/query",
  './widgets/IconBlock'
], function(
  lang,
  xhr,
  on,
  dom,
  domConstruct,
  domClass,
  array,
  RangeSlider,
  domStyle,
  event,
  query,
  IconBlock
) {

  var absMin = 1,
      absMax = 128,
      min = 16,
      max = 32,
      blocks = [];
  
  function sizeLimitChanged(value) {
    min = value[0];
    max = value[1];
    filterImages(min, max);
  }
  
  function showSizeLimit(count, total, min, max) {
    dom.byId("iconSizeIndicator").innerHTML =
      count + " of " + total + " images are between " + min + " and " + max + " pixels.";
  }
  
  function matches(min, max, block) {
    return min <= block.size && block.size <= max;
  }

  function filterImages(min, max) {
    var match = lang.partial(matches, min, max);
    var matching = _.filter(blocks, match);
    showSizeLimit(matching.length, blocks.length, min, max);
    _.each(blocks, function(block) {
      block[match(block) ? "show" : "hide"]();
    });
  }
  
  function finished() {
    filterImages(min, max);
    domStyle.set("iconsContainer", "display", "");
    domClass.remove('iconsContainer', 'loading');
    domStyle.set("loadInfo", "display", "none");
    
    var args = {
        value: [min, max],
        minimum: absMin,
        maximum: absMax,
        discreteValues: absMax - absMin + 1,
        style: "width: 300px",
        intermediateChanges: true,
        onChange: sizeLimitChanged
    };
    new dojox.form.HorizontalRangeSlider(args, "slider");
  }
  var count = 0;
  function imageLoaded(block, total) {
    var size = block.size;
    absMin = Math.min(absMin, size);
    absMax = Math.max(absMax, size);
    updateLoadInfo(total);
  }
  
  function updateLoadInfo(total) {
    var loaded = _.filter(blocks, function(b) { return b.loaded; });
    dom.byId('loadInfo').innerHTML = 'Loaded ' + loaded.length + " of " + total + " images.";
    if (total > 0 && total === loaded.length) {
      finished();
    }
  }
  
  function parse(uri) {
    
    domClass.add('iconsContainer', 'loading');

    function load(images) {
      domStyle.set("loadInfo", "display", "");
      updateLoadInfo(images.length);
      var container = dom.byId("images");
      
      _.each(images, function(image) {
        var block = new IconBlock(image);
        blocks.push(block);
        block.load = lang.partial(imageLoaded, block, images.length);
        domConstruct.place(block.domNode, container);
      });
    }
    
    xhr.get({
      url: "/api/imagescraper",
      content: { uri: uri },
      handleAs: "json",
      load: load
    });
    
  }

  on(dom.byId("test"), "click", function(evt) {
    event.stop(evt);
    var uri = "http://localhost:8080/static/imagetest/index.html";
    dom.byId("uri").value = uri;
    parse(uri);
  });
  
  on(dom.byId("uri"), "change", function(evt) {
    var uri = evt.target.value;
    if (uri !== "") { parse(uri); }
  });

});