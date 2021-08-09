/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cst8218.onei0212.game;
import cst8218.onei0212.entity.Sprite;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.List;
import javax.ejb.Remote;

/**
 * Defines the interface between the client and the server for handling 
 * user interaction with the game.
 * 
 * @author tgk
 */
@Remote
public interface SpriteSessionRemote {
    	List<Sprite> getSpriteList() throws RemoteException;
	void newSprite(MouseEvent e) throws RemoteException;
	int getHeight() throws RemoteException;
	int getWidth() throws RemoteException;
}
