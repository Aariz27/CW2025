package com.comp2042.game.events;

import com.comp2042.game.data.DownData;
import com.comp2042.game.data.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    DownData onHardDropEvent(MoveEvent event);

    void createNewGame();
}
