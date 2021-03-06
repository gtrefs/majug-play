package helper;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import java.util.stream.Collectors;

import models.Entry;
import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

import controllers.routes;

public abstract class JsonLinker {
	
	public static JsonLinker from(List<Entry> entries){
		return new EntriesCollectionResourceLinker(entries);
	}
	
	public static JsonLinker from(Entry entry){
		return new EntryResourceLinker(entry);
	}
	
	public static String getLocation(Entry entry){
		return routes.Entries.show(entry.id).url();
	}
	
	public abstract JsonNode to();
	
	protected JsonNode appendEntryLinks(JsonNode entry){
		checkArgument(entry.isObject(), "Entry is not an object node.");
		final List<Link> links = Lists.newArrayList(selfEntryLink(entry), entriesCollectionLink());
		((ObjectNode) entry).put("_links", Json.toJson(links));
		return entry;
	}
	
	private Link selfEntryLink(JsonNode entry) {
		final Integer id = entry.get("id").asInt();
		return Link.make(routes.Entries.show(id).url(), LinkTypes.SELF.asLowerCase());
	}
	
	private Link entriesCollectionLink() {
		return Link.make(routes.Entries.list().url(), LinkTypes.COLLECTION.asLowerCase());
	}
	
	private static class EntriesCollectionResourceLinker extends JsonLinker {
		
		private final List<Entry> entries;
		
		public EntriesCollectionResourceLinker(List<Entry> entries) {
			this.entries = entries;
		}

		@Override
		public JsonNode to() {
			final ObjectNode entriesCollection = Json.newObject();
			entriesCollection.put("_links", createLinks());
			entriesCollection.put("_embedded", createEmbeddedEntries());
			return entriesCollection;
		}
		
		private JsonNode createLinks() {
			final List<Link> links = Lists.newArrayList(selfCollectionLink());
			links.addAll(entryLinks());
			return Json.toJson(links);
		}
		
		private Link selfCollectionLink() {
			return Link.make(routes.Entries.list().url(), LinkTypes.SELF.asLowerCase());
		}
		
		private List<Link> entryLinks() {
			return entries.stream()
					.map(entry -> routes.Entries.show(entry.id).url())
					.map(url -> Link.make(url, LinkTypes.ITEM.asLowerCase()))
					.collect(Collectors.toList());
		}

		private JsonNode createEmbeddedEntries() {
			return Json.toJson(entries.stream()
					.map(Json::toJson)
					.map(this::appendEntryLinks)
					.collect(Collectors.toList()));
		}
	}
	
	private static class EntryResourceLinker extends JsonLinker {

		private final Entry entry;

		public EntryResourceLinker(Entry entry) {
			this.entry = entry;
		}

		@Override
		public JsonNode to() {
			return appendEntryLinks(Json.toJson(entry));
		}
		
	}
	
	private static class Link {
		public String href;
		public String rel;
		
		public static Link make(String href, String rel){
			final Link link = new Link();
			link.href = href;
			link.rel = rel;
			return link;
		}
	}
	
	private static enum LinkTypes{
		COLLECTION, ITEM, SELF;
		
		public String asLowerCase(){
			return toString().toLowerCase();
		}
	}
}