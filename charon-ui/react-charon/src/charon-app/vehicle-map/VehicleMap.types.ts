export type VehicleMapConfig = {
    mapType: "vesselfinder" | "none";
    config: {
        zoom: number;
        fleet: string;
        fleet_name: string;
        names: boolean;
        width: string;
        height: string;
        latitude: number;
        longitude: number;
    }
};