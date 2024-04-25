package com.arthurqueiroz;

import java.io.File;

import com.arthurqueiroz.dinamic.ProxyFactory;
import com.arthurqueiroz.interfaces.Playable;
import com.arthurqueiroz.services.PlayerService;
import com.arthurqueiroz.interfaces.Seekable;

public class ProxyApp {

    public static void main(String[] args) {
        ProxyFactory proxyFactory = new ProxyFactory(PlayerService.class.getPackage());

        File file = new File("SongPath");
        Playable player = proxyFactory.getBean(Playable.class);
        System.out.println("business.play(file.getPath())");
        System.out.println(player.play(file.getPath()));

        System.out.println();
        System.out.println("business.play(file)");
        System.out.println(player.play(file));

        System.out.println();
        System.out.println("business.play(file, 10)");
        System.out.println(player.play(file, 10));

        System.out.println();
        System.out.println("business.play(file, 10, 15)");
        System.out.println(player.play(file, 10, 15));


        Seekable seekablePlayer = proxyFactory.getBean(Seekable.class);
        System.out.println();
        System.out.println("seekablePlayer.seekTo(20)");
        System.out.println(seekablePlayer.seekTo(20));    
    }

}