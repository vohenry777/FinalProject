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

public class AliasTest {

	private static EntityManagerFactory emf;
	private EntityManager em;
	private Alias alias;

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
		alias = em.find(Alias.class, 1);
	}

	@AfterEach
	void tearDown() throws Exception {
		em.close();
	}

	@Test
	@DisplayName("Alias mapping")
	void test_alias_mapping() {
		assertNotNull(alias);
		assertEquals("Baya", alias.getName());
		assertEquals(1619, alias.getUser().getId());
//	---------------------+---------------------+---------------------+
//	| id | user_id | enabled | name | description | image_url                                                                          | created             | updated             |
//	+----+---------+---------+------+-------------+------------------------------------------------------------------------------------+---------------------+---------------------+
//	|  1 |    1619 |       1 | Baya |             | https://skilldistillery.com/wp-content/uploads/2016/02/skilldistillery_website.png | 2022-05-24 18:30:00 | 2022-05-24 18:30:00 |
//	+----+---------+---------+------+-------------+------------------------------------------------------------------------------------+---------------------+---------------------+
	}

	@Test
	@DisplayName("Test Alias m:m Game")
	void test_alias_game_mapping() {
		
//		 SELECT alias_id, COUNT(*) FROM alias_game GROUP BY alias_id ORDER BY COUNT(*) DESC;
//		 +----------+----------+
//		 | alias_id | COUNT(*) |
//		 +----------+----------+
//		 |       15 |        1 |
		
		// find example with more than one in list
		alias = em.find(Alias.class, 15);
		assertNotNull(alias);
		assertNotNull(alias.getGames());
		assertTrue(alias.getGames().size() > 0);

		// test both sides and no duplicates
		int expectedMatches = alias.getGames().size();
		int matches = 0;

		// each of server's clans
		for (Game game : alias.getGames()) {
			// each of the server's clan's servers
			for (Alias aliasGame : game.getAliases()) {
				// verify valid data
				if (aliasGame.getName().equals(alias.getName())) {
					matches++;
				}
			}
		}

		assertEquals(expectedMatches, matches);
	}

	@Test
	@DisplayName("Test Alias m:m Server")
	void test_alias_server_mapping() {

//		 SELECT alias_id, COUNT(*) FROM alias_server GROUP BY alias_id ORDER BY COUNT(*) DESC;
//		 +----------+----------+
//		 | alias_id | COUNT(*) |
//		 +----------+----------+
//		 |        1 |        4 |

		// find example with more than one in list
		alias = em.find(Alias.class, 1);
		assertNotNull(alias);
		assertNotNull(alias.getServers());
		assertTrue(alias.getServers().size() > 0);

		// test both sides and no duplicates
		int expectedMatches = alias.getServers().size();
		int matches = 0;

		// each of the alias's servers
		for (Server server : alias.getServers()) {
			// each of the alias's server's aliases
			for (Alias serverAlias : server.getAliases()) {
				// verify valid data
				if (serverAlias.getName().equals(alias.getName())) {
					matches++;
				}
			}
		}

		assertEquals(expectedMatches, matches);
	}

	@Test
	@DisplayName("Test Alias m:m Clan")
	void test_alias_clan_mapping() {

//		 SELECT alias_id, COUNT(*) FROM alias_clan GROUP BY alias_id ORDER BY COUNT(*) DESC;
//		 +----------+----------+
//		 | alias_id | COUNT(*) |
//		 +----------+----------+
//		 |     5507 |        8 |

		// find example with more than one in list
		alias = em.find(Alias.class, 5507);
		assertNotNull(alias);
		assertNotNull(alias.getClans());
		assertTrue(alias.getClans().size() > 0);

		// test both sides and no duplicates
		int expectedMatches = alias.getClans().size();
		int matches = 0;

		// each of the alias's clans
		for (Clan clan : alias.getClans()) {
			// each of the alias's clan's alises
			for (Alias clanAlias : clan.getAliases()) {
				// verify valid data
				if (clanAlias.getName().equals(alias.getName())) {
					matches++;
				}
			}
		}

		assertEquals(expectedMatches, matches);

	}

	@Test
	@DisplayName("Test Alias m:m Platform")
	void test_alias_platform_mapping() {
		// find example with more than one in list
		alias = em.find(Alias.class, 15);
		assertNotNull(alias);
		assertNotNull(alias.getPlatforms());
		assertTrue(alias.getPlatforms().size() > 0);

		// test both sides and no duplicates
		int matches = 0;
		int expectedMatches = alias.getPlatforms().size();

		// each of server's clans
		for (Platform platform : alias.getPlatforms()) {
			// each of the server's clan's servers
			for (Alias platformAlias : platform.getAliases()) {
				// verify valid data
				if (platformAlias.getName().equals(alias.getName())) {
					matches++;
				}
			}
		}

		assertEquals(expectedMatches, matches);

//	 SELECT alias_id, COUNT(*) FROM alias_platform GROUP BY alias_id ORDER BY COUNT(*) DESC;
//	 +----------+----------+
//	 | alias_id | COUNT(*) |
//	 +----------+----------+
//	 |       15 |        1 |
	}
}