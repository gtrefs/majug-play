package controllers;

import helper.JsonLinker;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import models.Entry;
import play.mvc.Controller;
import play.mvc.Result;

public class Entries extends Controller {

    public static Result list() {
    	final List<Entry> entries = Entry.find.all();
        return ok(JsonLinker.from(entries).to());
    }
    
    public static Result create() {
        return ok("Your new application is ready.");
    }
    
    public static Result show(Integer id) {
    	return Optional
    			.ofNullable(Entry.find.byId(id))
    			.map(entry -> ok(JsonLinker.from(entry).to()))
    			.orElse(notFound());
    }
    
    public static Result update(Integer id) {
        return ok("update id: " + id);
    }
    
    public static Result delete(Integer id) {
    	return Optional
    			.ofNullable(Entry.find.byId(id))
    			.map(Entries::deleteNoContent)
    			.orElse(notFound());
    }
    
    private static Status deleteNoContent(Entry entry){
    	entry.delete();
    	return noContent();
    }

}
