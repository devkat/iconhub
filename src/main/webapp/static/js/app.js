require({  
    packages: [  
        { name: 'dojo', location: 'PATH_TO_SCRIPTS/dojo' },  
        { name: 'bootstrap', location: 'PATH_TO_SCRIPTS/bootstrap' }  
    ]  
}, [   
    'bootstrap/Button',  
    'dojo/domReady!'  
], function(){  
  
}); 