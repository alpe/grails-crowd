package grailscrowd.core

import grailscrowd.core.Tag

class Tagging implements Comparable {

    static belongsTo = [member: Member, tag: Tag, project: GrailsProject]

    static def createFor(member, tagName, project) {
        def t = Tag.findByName(tagName)
        if (!t) {
            //Add the tag
            t = new Tag(name: tagName).save()
        }
        new Tagging(member: member, tag: t, project: project)
    }

    static def globalTagCounts() {
        withTagAndCountsMap {tag, counts ->
            counts[tag.name] = Tagging.countByTag(tag)
        }
    }

    static def tagCountsForMember(member) {
        withTagAndCountsMap {tag, counts ->
            def count = Tagging.countByTagAndMember(tag, member)
            if(count) {
                counts[tag.name] = count
            }
        }
    }

	// taggings are sorted alphabetically by tag name
    public int compareTo(Object obj) {
        int result = tag.name <=> obj.tag.name
        // must match "consistent with equals" contract for TreeSet
        return result?:this.hashCode()<=>obj.hashCode()

    }

    private static def withTagAndCountsMap(callable) {
        def tagCounts = new TreeMap()
        def tags = Tag.listOrderByName()
        tags.each {
            callable(it, tagCounts)
        }
        tagCounts
    }
}
