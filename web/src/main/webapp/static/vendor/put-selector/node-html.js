//>>built
define("put-selector/node-html",["dojo","dijit","dojox"],function(){function i(a){this.tag=a}function j(){}var f,k={};"base,link,meta,hr,br,wbr,img,embed,param,source,track,area,col,input,keygen,command".split(",").forEach(function(a){k[a]=!0});var d=i.prototype,g="";d.nodeType=1;d.put=function(){var a=[this];a.push.apply(a,arguments);return f.apply(null,a)};d.toString=function(a){var b=this.tag,c=k[b];return f.indentation&&!a?(a=g,g+=f.indentation,b=("html"==b?"<!DOCTYPE html>\n<html":"\n"+a+"<"+
b)+(this.attributes?this.attributes.join(""):"")+(this.className?' class="'+this.className+'"':"")+">"+(this.children?this.children.join(""):"")+(!this.mixed&&!c&&this.children?"\n"+a:"")+(c?"":"</"+b+">"),g=a,b):("html"==this.tag?"<!DOCTYPE html>\n<html":"<"+this.tag)+(this.attributes?this.attributes.join(""):"")+(this.className?' class="'+this.className+'"':"")+">"+(this.children?this.children.join(""):"")+(a||c?"":"</"+b+">")};d.sendTo=function(a){function b(h){var l=c(this);l&&a.write(l);h.tag?
(f.indentation?(a.write("\n"+d+h.toString(!0)),d+=f.indentation):a.write(h.toString(!0)),this.children=!0,e=h,h.pipe=b):a.write(h.toString())}function c(a){for(var c="";e!=a;){if(!e)throw Error("Can not add to an element that has already been streamed");var b=e.tag,g=k[b];f.indentation?(d=d.slice(f.indentation.length),g||(c+=(e.mixed||!e.children?"":"\n"+d)+"</"+b+">")):g||(c+="</"+b+">");e=e.parentNode}return c}"function"==typeof a&&(a={write:a,end:a});var e=this,d="";b.call(this,this);this.end=
function(b){a[b?"write":"end"](c(this)+"\n</"+this.tag+">")};return this};d.children=!1;d.attributes=!1;d.insertBefore=function(a,b){a.parentNode=this;if(this.pipe)return this.pipe(a);var c=this.children;if(!c)c=this.children=[];if(b)for(var e=0,d=c.length;e<d;e++)if(b==c[e]){a.nextSibling=b;if(0<e)c[e-1].nextSibling=a;return c.splice(e,0,a)}if(0<c.length)c[c.length-1].nextSibling=a;c.push(a)};d.appendChild=function(a){if("string"==typeof a)this.mixed=!0;if(this.pipe)return this.pipe(a);var b=this.children;
if(!b)b=this.children=[];b.push(a)};d.setAttribute=function(a,b){var c=this.attributes;if(!c)c=this.attributes=[];c.push(" "+a+'="'+b+'"')};d.removeAttribute=function(a){var b=this.attributes;if(b)for(var a=" "+a+"=",c=a.length,d=0,f=b.length;d<f;d++)if(b[d].slice(0,c)==a)return b.splice(d,1)};Object.defineProperties(d,{innerHTML:{get:function(){return this.children.join("")},set:function(a){this.mixed=!0;if(this.pipe)return this.pipe(a);this.children=[a]}}});j.prototype=new i;j.prototype.toString=
function(){return this.children?this.children.join(""):""};var m=/</g,n=/&/g;module.exports=function(a,b){f=a.exports=b().forDocument({createElement:function(a){return new i(a)},createTextNode:function(a){return("string"==typeof a?a:""+a).replace(m,"&lt;").replace(n,"&amp;")},createDocumentFragment:function(){return new j}},{test:function(){return!1}});f.indentation="  ";f.Page=function(a){return f("html").sendTo(a)}}});