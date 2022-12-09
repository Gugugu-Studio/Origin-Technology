package com.gugugu.oritech.world.block;

import com.gugugu.oritech.world.block.properties.DirectionProperty;
import com.gugugu.oritech.world.block.properties.IProperty;
import com.gugugu.oritech.util.math.Direction;

import java.util.List;

public class LogBlock extends Block{
    private static Direction getNowFace(Direction dir, Direction face) {
        return switch (dir) {
            case UP, DOWN -> face;
            case EAST -> switch (face) {
                case UP -> Direction.EAST;
                case DOWN -> Direction.WEST;
                case EAST -> Direction.DOWN;
                case WEST -> Direction.UP;
                case NORTH, SOUTH -> face;
            };
            case WEST -> switch (face) {
                case UP -> Direction.WEST;
                case DOWN -> Direction.EAST;
                case EAST -> Direction.UP;
                case WEST -> Direction.DOWN;
                case NORTH, SOUTH -> face;
            };
            case NORTH -> switch (face) {
                case UP -> Direction.NORTH;
                case DOWN -> Direction.SOUTH;
                case NORTH -> Direction.DOWN;
                case SOUTH -> Direction.UP;
                case EAST, WEST -> face;
            };
            case SOUTH -> switch (face) {
                case UP -> Direction.SOUTH;
                case DOWN -> Direction.NORTH;
                case NORTH -> Direction.UP;
                case SOUTH -> Direction.DOWN;
                case EAST, WEST -> face;
            };
        };
    }

    @Override
    public boolean shouldRenderFace(BlockState state, Direction face) {
        IProperty property = state.getProperty("dir");
        Direction dir = Direction.UP;
        if (property instanceof DirectionProperty) {
            dir = ((DirectionProperty) property).getValue();
        }
        return super.shouldRenderFace(state, getNowFace(dir, face));
    }

    @Override
    public List<IProperty> getProperties() {
        List<IProperty> properties = super.getProperties();
        properties.add(new DirectionProperty("dir", Direction.EAST));
        return properties;
    }
}
