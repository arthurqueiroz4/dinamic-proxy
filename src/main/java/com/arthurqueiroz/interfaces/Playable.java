package com.arthurqueiroz.interfaces;

import java.io.File;

public interface Playable {
    String play(String song);
    String play(File song);
    String play(File song, int start);
    String play(File song, int start, int finish);
}