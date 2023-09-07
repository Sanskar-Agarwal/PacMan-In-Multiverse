package src.pacman.portals;

public enum PortalColour {
    White,
    Yellow,
    DarkGold,
    DarkGray;

    public String getImageName() {
        switch (this) {
            case White: return "sprites/i_portalWhiteTile.png";
            case Yellow: return "sprites/j_portalYellowTile.png";
            case DarkGold: return "sprites/k_portalDarkGoldTile.png";
            case DarkGray: return "sprites/l_portalDarkGrayTile.png";
            default: {
                assert false;
            }
        }
        return null;
    }
}
