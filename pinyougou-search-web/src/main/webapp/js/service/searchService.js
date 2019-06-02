app.service('searchService',function($http){
	
	
	this.search=function(searchMap){
		alert(searchMap.keywords);
		return $http.post('itemsearch/search.do',searchMap);
	}
	
});