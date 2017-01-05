package de.fhg.iais.roberta.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.Group;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * DAO class to load and store programs objects. A DAO object is always bound to a session. This session defines the transactional context, in which the
 * database access takes place.
 *
 * @author Evgeniya
 */
public class GroupDao extends AbstractDao<Group> {
    private static final Logger LOG = LoggerFactory.getLogger(GroupDao.class);

    /**
     * create a new DAO for groups. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public GroupDao(DbSession session) {
        super(Group.class, session);
    }

    public Group persistGroup(String name, int ownerId) throws Exception {
        Assert.notNull(name);
        final Group group = loadGroup(name, ownerId);
        if ( group == null ) {
            //group = new Group(name);
            //this.session.save(user);
            return group;
        } else {
            return null;
        }
    }

    /**
     * load all groups persisted in the database which are owned by a user given
     *
     * @return the list of all groups, may be an empty list, but never null
     */
    public List<Group> loadAll(User owner) {
        int ownerId = owner.getId();
        Query hql = this.session.createQuery("from GROUP where OWNER_ID=:ownerId");
        hql.setEntity("ownerId", ownerId);
        @SuppressWarnings("unchecked")
        List<Group> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    //TODO: replace pairs with users
    public List<User> loadMembers(String groupName) {
        Query hql = this.session.createQuery("from USER_GROUP where NAME=:groupName");
        hql.setEntity("groupName", groupName);
        @SuppressWarnings("unchecked")
        List<User> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    public Group loadGroup(String name, int ownerId) {
        Assert.notNull(name);
        final Query hql = this.session.createQuery("from User where account=:account");
        hql.setString("name", name);

        return checkGroupExistance(hql);
    }

    private Group checkGroupExistance(Query hql) {
        @SuppressWarnings("unchecked")
        final List<Group> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        if ( il.size() == 0 ) {
            return null;
        } else {
            return il.get(0);
        }
    }

    public int deleteByName(String name, int ownerId) {
        final Group toBeDeleted = loadGroup(name, ownerId);
        if ( toBeDeleted == null ) {
            return 0;
        } else {
            this.session.delete(toBeDeleted);
            return 1;
        }
    }

    public Pair<Key, Group> persistOwnGroup(String name, int userId) {
        Assert.notNull(name);
        Assert.notNull(userId);
        Group group = load(userId);
        if ( group == null ) {
            // save as && the program doesn't exist.
            group = new Group(name, userId);
            this.session.save(group);
            return Pair.of(Key.GROUP_SAVE_SUCCESS, group); // the only legal key if success
        } else {
            return Pair.of(Key.GROUP_SAVE_AS_ERROR_PROGRAM_EXISTS, null);
        }
    }

}
