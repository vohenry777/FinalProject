package com.skilldistillery.gaminghub.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

	private static EntityManagerFactory emf;
	private EntityManager em;
	private User user;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("JPAGamingHub");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		emf.close();
	}

	@BeforeEach
	void setUp() throws Exception {
		em = emf.createEntityManager();
		user = em.find(User.class, 1);
	}

	@AfterEach
	void tearDown() throws Exception {
		em.close();
	}

	@Test
	@DisplayName("User mapping")
	void test_user_mapping() {

//		SELECT id, username FROM user WHERE id = 1;
//		+----+----------+
//		| id | username |
//		+----+----------+
//		|  1 | admin    |
//		+----+----------+

		assertNotNull(user);
		assertEquals("admin", user.getUsername());
	}

	@DisplayName("User Alias oTm mapping")
	@Test
	void test_user_alias_mapping() {

//		SELECT COUNT(*) FROM alias WHERE user_id = 2;
//		+----------+
//		| COUNT(*) |
//		+----------+
//		|        3 |
//		+----------+

		user = em.find(User.class, 2);
		assertNotNull(user);
		assertNotNull(user.getAliases());
		assertTrue(user.getAliases().size() > 0);

		// test both sides and no duplicates
		int matches = 0;
		int expectedMatches = user.getAliases().size();

		// each of the user's meetups
		for (Alias alias : user.getAliases()) {
			// verify valid data
			if (alias.getUser().getUsername().equals(user.getUsername())) {
				matches++;
			}
		}

		assertEquals(expectedMatches, matches);
	}

	@DisplayName("User Friend mapping")
	@Test
	void test_user_friend_mapping() {

//		SELECT user_id, COUNT(*) FROM user_friend GROUP BY user_id ORDER BY COUNT(*) DESC;
//		+---------+----------+
//		| user_id | COUNT(*) |
//		+---------+----------+
//		|     675 |       70 |

		// user has friends
		user = em.find(User.class, 675);
		assertNotNull(user);
		assertNotNull(user.getFriends());
		assertTrue(user.getFriends().size() > 0);
		assertEquals(70, user.getFriends().size());

		// user has each friend only one time
		int matches = 0;
		int expectedMatches = user.getFriends().size();
		for (User usersFriend : user.getFriends()) {

			// user's friend has friends
			assertNotNull(usersFriend.getFriends());
			assertTrue(usersFriend.getFriends().size() > 0);

			// find match by usernames
			for (User friendsFriend : usersFriend.getFriends()) {
				if (friendsFriend.getUsername().equals(user.getUsername())) {
					matches++;
				}
			}
		}

		// number of times user's friends have this user as a friend
		assertEquals(expectedMatches, matches);
	}

	@DisplayName("UserFriend ID")
	@Test
	void test_user_friend_id() {

		// find user with only 1 friend via user_friend table
		// composite id is basically user.friend.id ( user.user.{id1, id2} )

//		SELECT * FROM user_friend WHERE user_id = 2;
//		+---------+-----------+---------------------+
//		| user_id | friend_id | created             |
//		+---------+-----------+---------------------+
//		|       2 |      1957 | 2019-01-20 19:37:35 |
//		+---------+-----------+---------------------+

		// create new composite id
		UserFriendId id = new UserFriendId();
		id.setUserId(2);
		id.setFriendId(1957);

		// this works just like em.find(class, int id)
		UserFriend userFriend = em.find(UserFriend.class, id);
		assertNotNull(userFriend);

		// toString puts "T" where there was a space in mysql
		assertEquals("2019-01-20T19:37:35", userFriend.getCreated().toString());
	}

	@DisplayName("User Blocked mapping")
	@Test
	void test_user_blocked_mapping() {

//		SELECT user_id, COUNT(blocked_user_id) FROM blocked_user GROUP BY user_id ORDER BY COUNT(*) DESC;
//		+---------+------------------------+
//		| user_id | COUNT(blocked_user_id) |
//		+---------+------------------------+
//		|     408 |                     16 |

		user = em.find(User.class, 408);
		assertNotNull(user);
		assertNotNull(user.getBlockedUsers());
		assertTrue(user.getBlockedUsers().size() > 0);
		assertEquals(16, user.getBlockedUsers().size());

		// note: this is not a bidirectional relationship, so blockedUser knows nothing
		// about user
		for (User blockedUser : user.getBlockedUsers()) {
			// verify valid data
			assertNotNull(blockedUser.getUsername());
		}
	}

	@DisplayName("User Blocked mapping")
	@Test
	void test_user_blocked_id() {

//		SELECT * FROM blocked_user WHERE user_id = 408;
//		+---------+-----------------+---------------------+--------+
//		| user_id | blocked_user_id | created             | reason |
//		+---------+-----------------+---------------------+--------+
//		|     408 |              41 | 2022-05-29 08:13:17 |        |

		user = em.find(User.class, 408);
		assertNotNull(user);
		assertNotNull(user.getBlockedUsers());
		assertTrue(user.getBlockedUsers().size() > 0);

		// create new composite id
		BlockedUserId id = new BlockedUserId();
		id.setUserId(408);
		id.setBlockedUserId(41);

		// this works just like em.find(class, int id)
		BlockedUser blockedUser = em.find(BlockedUser.class, id);
		assertNotNull(blockedUser);

		// verify valid data
		assertEquals("2022-05-29T08:13:17", blockedUser.getCreated().toString());
	}

	@DisplayName("User --> Chat Mapping")
	@Test
	void test_user_to_chat_mapping() {

//		SELECT id, created_by_user_id, enabled, title, description FROM chat;
//		+----+--------------------+---------+--------------------+------------------------------+
//		| id | created_by_user_id | enabled | title              | description                  |
//		+----+--------------------+---------+--------------------+------------------------------+
//		|  1 |                  1 |       1 | Hell's Angels Chat | Guild Chat for Hell's Angels |
//		|  2 |                  7 |       1 | Hell's Angels Chat | Guild Chat for Hell's Angels |
//		+----+--------------------+---------+--------------------+------------------------------+

		user = em.find(User.class, 1);
		assertNotNull(user);
		assertNotNull(user.getChats());

		// test both sides and no duplicates
		int matches = 0;
		int expectedMatches = user.getChats().size();

		// each of the user's chats
		for (Chat chat : user.getChats()) {
			assertNotNull(chat.getAllUsers());
			assertTrue(chat.getAllUsers().size() > 0);
			// each of the user's chat's users
			for (User chatUser : chat.getAllUsers()) {
				if (chatUser.getUsername().equals(user.getUsername())) {
					matches++;
				}
			}
		}
		
		// TODO: chat single user (creatingUser) mapping

		assertEquals(expectedMatches, matches);
	}

	@DisplayName("User --> Location ManyToMany Mapping")
	@Test
	void test_user_to_location_mapping() {

//		SELECT user_id, COUNT(*) FROM user_location GROUP BY user_id ORDER BY COUNT(*) DESC;
//		+---------+----------+
//		| user_id | COUNT(*) |
//		+---------+----------+
//		|    1024 |        2 |

		user = em.find(User.class, 1024);
		assertNotNull(user);
		assertNotNull(user.getLocations());
		assertTrue(user.getLocations().size() > 0);

		// test both sides and no duplicates
		int matches = 0;
		int expectedMatches = user.getLocations().size();

		// each of the user's locations
		for (Location location : user.getLocations()) {
			assertNotNull(location.getUsers());
			assertTrue(location.getUsers().size() > 0);
			// each of the user's location's users
			for (User locationUser : location.getUsers()) {
				if (locationUser.getUsername().equals(user.getUsername())) {
					matches++;
				}
			}
		}

		assertEquals(expectedMatches, matches);
	}

	@DisplayName("User --> Equipment ManyToMany Mapping")
	@Test
	void test_user_to_equipment_mapping() {

//		SELECT user_id, count(*) FROM user_equipment GROUP BY user_id ORDER BY count(*) desc;
//		+---------+----------+
//		| user_id | count(*) |
//		+---------+----------+
//		|      16 |        3 |

		user = em.find(User.class, 16);
		assertNotNull(user);
		assertNotNull(user.getEquipments());
		assertTrue(user.getEquipments().size() > 0);

		// test both sides and no duplicates
		int matches = 0;
		int expectedMatches = user.getEquipments().size();

		// each of the user's equipements
		for (Equipment equipment : user.getEquipments()) {
			assertNotNull(equipment.getUsers());
			assertTrue(equipment.getUsers().size() > 0);
			// each of the user's equipment's users
			for (User equipmentUser : equipment.getUsers()) {
				if (equipmentUser.getFirstName().equals(user.getFirstName())) {
					matches++;
				}
			}
		}
		assertEquals(expectedMatches, matches);
	}

	@DisplayName("User --> Meetup OneToMany Mapping")
	@Test
	void test_user_to_meetup_mapping() {

//		SELECT * from meetup WHERE user_id=398;
//		+----+-------------+---------+--------------+---------------------+----------+-------------+---------------------+---------------------+
//		| id | timezone_id | user_id | name         | date                | capacity | description | created             | updated             |
//		+----+-------------+---------+--------------+---------------------+----------+-------------+---------------------+---------------------+
//		|  1 |           8 |     398 | Free for all | 2022-05-03 20:00:00 |       36 |             | 2022-05-24 18:30:00 | 2022-05-24 18:30:00 |
//		+----+-------------+---------+--------------+---------------------+----------+-------------+---------------------+---------------------+

		user = em.find(User.class, 398);
		assertNotNull(user);
		assertNotNull(user.getMeetups());
		assertTrue(user.getMeetups().size() > 0);

		// test both sides and no duplicates
		int matches = 0;
		int expectedMatches = user.getMeetups().size();

		// each of the user's meetups
		for (Meetup meetup : user.getMeetups()) {
			// verify valid data
			if (meetup.getUser().getUsername().equals(user.getUsername())) {
				matches++;
			}
		}

		assertEquals(expectedMatches, matches);
	}

	@DisplayName("User --> UserEndorsement 1:m Mapping")
	@Test
	void test_userendorsement_to_endorsement_mapping() {

//		SELECT user_id, COUNT(*) FROM user_endorsement GROUP BY user_id ORDER BY COUNT(*) DESC;
//		+---------+----------+
//		| user_id | COUNT(*) |
//		+---------+----------+
//		|     485 |       11 |

		user = em.find(User.class, 485);
		assertNotNull(user);
		assertNotNull(user.getSentUserEndorsements());
		assertTrue(user.getSentUserEndorsements().size() > 0);

//		int matches = 0;
//		int expectedMatches = user.getSentEndorsements().size();

		// create new composite id
		UserEndorsementId id = new UserEndorsementId();
		id.setUserId(485);
		id.setEndorsedUserId(137);
		id.setEndorsementId(5);

		UserEndorsement userEndorsement = em.find(UserEndorsement.class, id);
		assertNotNull(userEndorsement);

		// verify valid data
		assertEquals("2022-05-24T18:30", userEndorsement.getCreated().toString());

	}

	@DisplayName("User --> Endorsement mTm Mapping")
	@Test
	void test_user_to_endorsement_mapping() {
		user = em.find(User.class, 485);
		assertNotNull(user);
		assertNotNull(user.getSentUserEndorsements());
		assertTrue(user.getSentUserEndorsements().size() > 0);
		assertEquals(11, user.getSentUserEndorsements().size());
		int matches = 0;
		int expectedMatches = user.getSentUserEndorsements().size();
//		for(UserEndorsement userEndorsement: user.getSentEndorsements()) {
//			Endorsement endorsement = userEndorsement.getEndorsement();
//			assertNotNull(endorsement);
//			for(UserEndorsement endorsingUserEndorsement: endorsement.getEndorsingUsers()) {
//				if(endorsingUserEndorsement.getEndorsingUser().getUsername().equals(user.getUsername())) {
//					matches++;
//				}
//			}
//		}
//		assertEquals(expectedMatches, matches);
	}
}
