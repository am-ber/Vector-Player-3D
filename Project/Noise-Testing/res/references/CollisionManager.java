package com.vexoid.game.entity;

import com.vexoid.game.SoundManager;
import com.vexoid.game.entity.bosses.Boss1;
import com.vexoid.game.entity.bullets.Blue_Bullet2;
import com.vexoid.game.entity.bullets.Red_Bullet2;
import com.vexoid.game.entity.bullets.Yellow_Bullet2;
import com.vexoid.game.entity.enemies.type1.BasicEnemy;
import com.vexoid.game.entity.enemies.type2.AdvancedEnemy;
import com.vexoid.game.entity.enemies.type2.BasicLaserEnemy;
import com.vexoid.game.level.LevelManager;

public class CollisionManager {
	
	EntityManager entityManager;
	public CollisionManager(EntityManager entityManager){
		this.entityManager = entityManager;
	}
	//	Basic Enemy
	public void checkCollisionsBasicEnemy(float blueBullet, float redBullet, float yellowBullet){
		for (BasicEnemy e : entityManager.getBasicEnemies()) {
			for (Blue_Bullet2 m : entityManager.getPlayerBlueBullets()) {
				if (e.getBounds().overlaps(m.getBounds())) {
					e.decreaseHealth(blueBullet * entityManager.getHealthMultiplier());;
					entityManager.getEntity().removeValue(m, false);
					entityManager.doBlastEffect(m.pos.cpy(),10,m.texture, "blue");
					SoundManager.hit1.play(0.6f);
					if (e.entityDied){
						SoundManager.hit6.play(0.4f);
						entityManager.addToScore(100);
						LevelManager.getCurrentLevel().addToLevelScore(100);
						entityManager.doExplosion(e.pos.cpy(), 25, e.texture, 5, "blue");
						entityManager.getEntity().removeValue(e, false);
					}
				}
			}
			for (Red_Bullet2 m : entityManager.getPlayerRedBullets()) {
				if (e.getBounds().overlaps(m.getBounds())) {
					e.decreaseHealth(redBullet * entityManager.getHealthMultiplier());;
					entityManager.getEntity().removeValue(m, false);
					entityManager.doBlastEffect(m.pos.cpy(),10,m.texture, "red");
					SoundManager.hit1.play(0.6f);
					if (e.entityDied){
						entityManager.addToScore(100);
						LevelManager.getCurrentLevel().addToLevelScore(100);
						entityManager.doExplosion(e.pos.cpy(), 25, e.texture, 5, "red");
						entityManager.getEntity().removeValue(e, false);
					}
				}
			}
			for (Yellow_Bullet2 m : entityManager.getPlayerYellowBullets()) {
				if (e.getBounds().overlaps(m.getBounds())) {
					e.decreaseHealth(yellowBullet * entityManager.getHealthMultiplier());;
					entityManager.getEntity().removeValue(m, false);
					entityManager.doBlastEffect(m.pos.cpy(),10,m.texture, "yellow");
					SoundManager.hit1.play(0.6f);
					if (e.entityDied){
						entityManager.addToScore(100);
						LevelManager.getCurrentLevel().addToLevelScore(100);
						entityManager.doExplosion(e.pos.cpy(), 25, e.texture, 5, "yellow");
						entityManager.getEntity().removeValue(e, false);
					}
				}
			}
			if (e.getBounds().overlaps(entityManager.getPlayer().getBounds())){
				entityManager.getPlayer().decreaseHealth(25 * entityManager.getDamageMultiplier());
				SoundManager.hit2.play(0.3f);
				entityManager.getEntity().removeValue(e, false);
				entityManager.doBlastEffect(e.pos.cpy(),10,e.texture, "red");
			}
		}
	}
	//	Advanced Manager
	public void checkCollisionsAdvancedEnemy(float blueBullet, float redBullet, float yellowBullet){
		for (AdvancedEnemy e : entityManager.getAdvancedEnemies()) {
			for (Blue_Bullet2 m : entityManager.getPlayerBlueBullets()) {
				if (e.getBounds().overlaps(m.getBounds())) {
					e.decreaseHealth(blueBullet * entityManager.getHealthMultiplier());;
					entityManager.getEntity().removeValue(m, false);
					entityManager.doBlastEffect(m.pos.cpy(),10,m.texture, "blue");
					SoundManager.hit1.play(0.6f);
					if (e.entityDied){
						entityManager.addToScore(250);
						LevelManager.getCurrentLevel().addToLevelScore(250);
						entityManager.doExplosion(e.pos.cpy(), 25, e.texture, 5, "blue");
						entityManager.getEntity().removeValue(e, false);
					}
				}
			}
			for (Red_Bullet2 m : entityManager.getPlayerRedBullets()) {
				if (e.getBounds().overlaps(m.getBounds())) {
					e.decreaseHealth(redBullet * entityManager.getHealthMultiplier());;
					entityManager.getEntity().removeValue(m, false);
					entityManager.doBlastEffect(m.pos.cpy(),10,m.texture, "red");
					SoundManager.hit1.play(0.6f);
					if (e.entityDied){
						entityManager.addToScore(250);
						LevelManager.getCurrentLevel().addToLevelScore(250);
						entityManager.doExplosion(e.pos.cpy(), 25, e.texture, 5, "red");
						entityManager.getEntity().removeValue(e, false);
					}
				}
			}
			for (Yellow_Bullet2 m : entityManager.getPlayerYellowBullets()) {
				if (e.getBounds().overlaps(m.getBounds())) {
					e.decreaseHealth(yellowBullet * entityManager.getHealthMultiplier());;
					entityManager.getEntity().removeValue(m, false);
					entityManager.doBlastEffect(m.pos.cpy(),10,m.texture, "yellow");
					SoundManager.hit1.play(0.6f);
					if (e.entityDied){
						SoundManager.hit6.play(0.4f);
						entityManager.addToScore(250);
						LevelManager.getCurrentLevel().addToLevelScore(250);
						entityManager.doExplosion(e.pos.cpy(), 25, e.texture, 5, "yellow");
						entityManager.getEntity().removeValue(e, false);
					}
				}
			}
			if (e.getBounds().overlaps(entityManager.getPlayer().getBounds())){
				entityManager.getPlayer().decreaseHealth(25 * entityManager.getDamageMultiplier());
				SoundManager.hit2.play(0.3f);
				entityManager.getEntity().removeValue(e, false);
				entityManager.doBlastEffect(e.pos.cpy(),10,e.texture, "red");
			}
		}
	}
	// Laser Enemy
	public void checkCollisionsBasicLaserEnemy(float blueBullet, float redBullet, float yellowBullet){
		for (BasicLaserEnemy e : entityManager.getBasicLaserEnemies()) {
			for (Blue_Bullet2 m : entityManager.getPlayerBlueBullets()) {
				if (e.getBounds().overlaps(m.getBounds())) {
					e.decreaseHealth(blueBullet * entityManager.getHealthMultiplier());;
					entityManager.getEntity().removeValue(m, false);
					entityManager.doBlastEffect(m.pos.cpy(),10,m.texture, "blue");
					SoundManager.hit1.play(0.6f);
					if (e.entityDied){
						entityManager.addToScore(350);
						LevelManager.getCurrentLevel().addToLevelScore(350);
						entityManager.doExplosion(e.pos.cpy(), 25, e.texture, 5, "blue");
						entityManager.getEntity().removeValue(e, false);
					}
				}
			}
			for (Red_Bullet2 m : entityManager.getPlayerRedBullets()) {
				if (e.getBounds().overlaps(m.getBounds())) {
					e.decreaseHealth(redBullet * entityManager.getHealthMultiplier());;
					entityManager.getEntity().removeValue(m, false);
					entityManager.doBlastEffect(m.pos.cpy(),10,m.texture, "red");
					SoundManager.hit1.play(0.6f);
					if (e.entityDied){
						entityManager.addToScore(350);
						LevelManager.getCurrentLevel().addToLevelScore(350);
						entityManager.doExplosion(e.pos.cpy(), 25, e.texture, 5, "red");
						entityManager.getEntity().removeValue(e, false);
					}
				}
			}
			for (Yellow_Bullet2 m : entityManager.getPlayerYellowBullets()) {
				if (e.getBounds().overlaps(m.getBounds())) {
					e.decreaseHealth(yellowBullet * entityManager.getHealthMultiplier());;
					entityManager.getEntity().removeValue(m, false);
					entityManager.doBlastEffect(m.pos.cpy(),10,m.texture, "yellow");
					SoundManager.hit1.play(0.6f);
					if (e.entityDied){
						SoundManager.hit5.play(0.4f);
						entityManager.addToScore(350);
						LevelManager.getCurrentLevel().addToLevelScore(350);
						entityManager.doExplosion(e.pos.cpy(), 25, e.texture, 5, "yellow");
						entityManager.getEntity().removeValue(e, false);
					}
				}
			}
			if (e.getBounds().overlaps(entityManager.getPlayer().getBounds())){
				entityManager.getPlayer().decreaseHealth(25 * entityManager.getDamageMultiplier());
				SoundManager.hit2.play(0.3f);
				entityManager.getEntity().removeValue(e, false);
				entityManager.doBlastEffect(e.pos.cpy(),10,e.texture, "red");
			}
		}
	}
	//	Boss
	public void checkCollisionsBoss1(float blueBullet, float redBullet, float yellowBullet){
		for (Boss1 e : entityManager.getBoss1()) {
			for (Blue_Bullet2 m : entityManager.getPlayerBlueBullets()) {
				if (e.getBounds().overlaps(m.getBounds())) {
					e.decreaseHealth(blueBullet * entityManager.getHealthMultiplier());;
					entityManager.getEntity().removeValue(m, false);
					entityManager.doBlastEffect(m.pos.cpy(),10,m.texture, "blue");
					SoundManager.hit1.play(0.7f);
					if (e.entityDied){
						entityManager.addToScore(1000);
						LevelManager.getCurrentLevel().addToLevelScore(1000);
						entityManager.doExplosion(e.pos.cpy(), 25, e.texture, 5, "blue");
						entityManager.getEntity().removeValue(e, false);
					}
				}
			}
			for (Red_Bullet2 m : entityManager.getPlayerRedBullets()) {
				if (e.getBounds().overlaps(m.getBounds())) {
					e.decreaseHealth(redBullet * entityManager.getHealthMultiplier());;
					entityManager.getEntity().removeValue(m, false);
					entityManager.doBlastEffect(m.pos.cpy(),10,m.texture, "red");
					SoundManager.hit1.play(0.6f);
					if (e.entityDied){
						SoundManager.hit5.play(0.6f);
						entityManager.addToScore(1000);
						LevelManager.getCurrentLevel().addToLevelScore(1000);
						entityManager.doExplosion(e.pos.cpy(), 25, e.texture, 5, "red");
						entityManager.getEntity().removeValue(e, false);
					}
				}
			}
			for (Yellow_Bullet2 m : entityManager.getPlayerYellowBullets()) {
				if (e.getBounds().overlaps(m.getBounds())) {
					e.decreaseHealth(yellowBullet * entityManager.getHealthMultiplier());;
					entityManager.getEntity().removeValue(m, false);
					entityManager.doBlastEffect(m.pos.cpy(),10,m.texture, "yellow");
					SoundManager.hit1.play(0.6f);
					if (e.entityDied){
						entityManager.addToScore(1000);
						LevelManager.getCurrentLevel().addToLevelScore(1000);
						entityManager.doExplosion(e.pos.cpy(), 25, e.texture, 5, "yellow");
						entityManager.getEntity().removeValue(e, false);
					}
				}
			}
			if (e.getBounds().overlaps(entityManager.getPlayer().getBounds())){
				entityManager.getPlayer().decreaseHealth(25 * entityManager.getDamageMultiplier());
				SoundManager.hit2.play(0.3f);
				entityManager.doBlastEffect(e.pos.cpy(),10,e.texture, "red");
			}
		}
	}
}
