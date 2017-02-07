package de.fhg.iais.roberta.persistence.bo;

import javax.persistence.Column;

//TODO: add messages about groups to html

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import de.fhg.iais.roberta.util.dbc.Assert;

@Entity
@Table(name = "GROUPS")
public class Groups implements WithSurrogateId {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "OWNER_ID")
    private int ownerId;

    @Column(name = "NAME")
    private String name;

    protected Groups() {
        // Hibernate
    }

    public Groups(String name, int ownerId) {
        Assert.notNull(name);
        Assert.notNull(ownerId);
        this.name = name;
        this.ownerId = ownerId;
    }

    /**
     * create a new group
     *
     * @param name the name of the group, not null
     * @param owner the user who created and thus owns the program
     */

    @Override
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getOwnerId() {
        return this.ownerId;
    }

    @Override
    public String toString() {
        return "Group [id=" + this.id + ", ownerId=" + this.ownerId;
    }

}