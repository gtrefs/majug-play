package controllers;

import helper.JsonLinker;

import java.util.List;
import java.util.Optional;

import models.Entry;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class Entries extends Controller {

    public static Result list() {
    	final List<Entry> entries = Entry.find.all();
        return ok(JsonLinker.from(entries).to());
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result create() {
    	return Optional.ofNullable(request().body().asJson())
    			.map(node -> Json.fromJson(node, Entry.class))
    			.map(Entries::saveAndSetLocationHeader)
    			.map(entry -> created(JsonLinker.from(entry).to()))
    			.orElse(badRequest());
    }
    
    private static Entry saveAndSetLocationHeader(Entry entry){
    	entry.save();
    	response().setHeader(LOCATION, JsonLinker.getLocation(entry));
    	return entry;
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
