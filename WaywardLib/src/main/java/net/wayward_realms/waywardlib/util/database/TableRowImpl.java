package net.wayward_realms.waywardlib.util.database;

public abstract class TableRowImpl implements TableRow {

    private int id;

    public TableRowImpl(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
