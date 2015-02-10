package controllers;

import helper.JsonLinker;

import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import models.Entry;
import play.data.validation.Validation;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import controllers.actions.CatchPersistenceAndValidationExceptionAction;

public class Entries extends Controller {

    public static Result list() {
    	final List<Entry> entries = Entry.find.all();
        return ok(JsonLinker.from(entries).to());
    }
    
    @With(CatchPersistenceAndValidationExceptionAction.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result create() {
    	return Optional.ofNullable(request().body().asJson())
    			.map(node -> Json.fromJson(node, Entry.class))
    			.map(Entries::saveAndSetLocationHeader)
    			.map(entry -> created(JsonLinker.from(entry).to()))
    			.orElse(badRequest());
    }
    
    private static Entry saveAndSetLocationHeader(Entry entry){
    	validate(entry);
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
    
    @With(CatchPersistenceAndValidationExceptionAction.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result update(Integer id) {
    	return Optional.ofNullable(Entry.find.byId(id))
		.map(Entries::update)
		.orElse(notFound());
    }
    
    private static Status update(Entry entry){
    	final Entry updatedEntry = Json.fromJson(request().body().asJson(), Entry.class);
    	validate(updatedEntry);
    	entry.setMessage(updatedEntry.message);
    	entry.setTitle(updatedEntry.title);
    	entry.update();
    	return noContent();
    }
    
    private static void validate(Entry entry) {
    	if(!Validation.getValidator().validate(entry).isEmpty()){
    		throw new ValidationException();
    	}
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