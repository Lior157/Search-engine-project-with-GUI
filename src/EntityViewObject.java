public class EntityViewObject {
    String Entity;
    String rank;

    public EntityViewObject(String entity, String rank) {
        Entity = entity;
        this.rank = rank;
    }


    public String getEntity() {
        return Entity;
    }

    public void setEntity(String entity) {
        Entity = entity;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
