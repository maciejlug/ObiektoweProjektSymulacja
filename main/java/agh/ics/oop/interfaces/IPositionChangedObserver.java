package agh.ics.oop.interfaces;
import agh.ics.oop.move.Vector2d;
import agh.ics.oop.objects.Animal;

public interface IPositionChangedObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal);
}