define([], function(s) {
  
  return {
    create: function(constructor, args) {
      
      function F() {
        return constructor.apply(this, args);
      }
      F.prototype = constructor.prototype;
      return new F();

    }
  };
  
});