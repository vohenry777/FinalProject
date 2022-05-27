package com.skilldistillery.gaminghub.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

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
		assertNotNull(user);
		assertEquals("admin", user.getUsername());
	}

	@DisplayName("User Alias mapping")
	@Test
	void test_user_alias_mapping() {

//		SELECT * FROM user JOIN alias ON alias.user_id = user.id WHERE user.id = 2;
//		+----+---------+-----------+---------------+--------------------------------------------------------------+---------------------------+------------+-------------+-----------+-------------+------------------------------------------------------------------------------------+---------------------+---------------------+------+---------+---------+---------+-------------+------------------------------------------------------------------------------------+---------------------+---------------------+
//		| id | enabled | role      | username      | password                                                     | email                     | first_name | middle_name | last_name | description | image_url                                                                          | created             | updated             | id   | user_id | enabled | name    | description | image_url                                                                          | created             | updated             |
//		+----+---------+-----------+---------------+--------------------------------------------------------------+---------------------------+------------+-------------+-----------+-------------+------------------------------------------------------------------------------------+---------------------+---------------------+------+---------+---------+---------+-------------+------------------------------------------------------------------------------------+---------------------+---------------------+
//		|  2 |       1 | ROLE_USER | bamboogateway | $2a$10$LNtqBOXd./fZWzfKaAj40uqdwZ2FX0KI2JUKSDLBVs3efyTCBvf6a | coleman.burrows@gmail.com | Coleman    |             | Burrows   |             | https://skilldistillery.com/wp-content/uploads/2016/02/skilldistillery_website.png | 2022-05-24 18:30:00 | 2022-05-24 18:30:00 | 1955 |       2 |       1 | Timothy |             | https://skilldistillery.com/wp-content/uploads/2016/02/skilldistillery_website.png | 2022-05-24 18:30:00 | 2022-05-24 18:30:00 |
//		|  2 |       1 | ROLE_USER | bamboogateway | $2a$10$LNtqBOXd./fZWzfKaAj40uqdwZ2FX0KI2JUKSDLBVs3efyTCBvf6a | coleman.burrows@gmail.com | Coleman    |             | Burrows   |             | https://skilldistillery.com/wp-content/uploads/2016/02/skilldistillery_website.png | 2022-05-24 18:30:00 | 2022-05-24 18:30:00 | 2150 |       2 |       1 | Greyson |             | https://skilldistillery.com/wp-content/uploads/2016/02/skilldistillery_website.png | 2022-05-24 18:30:00 | 2022-05-24 18:30:00 |
//		|  2 |       1 | ROLE_USER | bamboogateway | $2a$10$LNtqBOXd./fZWzfKaAj40uqdwZ2FX0KI2JUKSDLBVs3efyTCBvf6a | coleman.burrows@gmail.com | Coleman    |             | Burrows   |             | https://skilldistillery.com/wp-content/uploads/2016/02/skilldistillery_website.png | 2022-05-24 18:30:00 | 2022-05-24 18:30:00 | 3988 |       2 |       1 | Wigglz  |             | https://skilldistillery.com/wp-content/uploads/2016/02/skilldistillery_website.png | 2022-05-24 18:30:00 | 2022-05-24 18:30:00 |
//		+----+---------+-----------+---------------+--------------------------------------------------------------+---------------------------+------------+-------------+-----------+-------------+------------------------------------------------------------------------------------+---------------------+---------------------+------+---------+---------+---------+-------------+------------------------------------------------------------------------------------+---------------------+---------------------+

		user = em.find(User.class, 2);
		assertNotNull(user);
		assertEquals("bamboogateway", user.getUsername());
		assertNotNull(user.getAliases());
		assertEquals(3, user.getAliases().size());

		for (Alias alias : user.getAliases()) {
			assertEquals("bamboogateway", alias.getUser().getUsername());
		}
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
		// composite key is basically user.friend.id ( user.user.{id1, id2} )

//		SELECT * FROM user_friend WHERE user_id = 2;
//		+---------+-----------+---------------------+
//		| user_id | friend_id | created             |
//		+---------+-----------+---------------------+
//		|       2 |      1957 | 2019-01-20 19:37:35 |
//		+---------+-----------+---------------------+

		// create new composite id as above
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
		assertNotNull(user.getBlocks());
		assertTrue(user.getBlocks().size() > 0);
		assertEquals(16, user.getBlocks().size());

		for (User blockedUser : user.getBlocks()) {
			// valid data test
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
		assertNotNull(user.getBlocks());

		// create new composite id as above
		BlockedUserId id = new BlockedUserId();
		id.setUserId(408);
		id.setBlockedUserId(41);

		// this works just like em.find(class, int id)
		BlockedUser blockedUser = em.find(BlockedUser.class, id);
		assertNotNull(blockedUser);

		assertEquals("2022-05-29T08:13:17", blockedUser.getCreated().toString());
	}

}