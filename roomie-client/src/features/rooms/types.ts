export type RoomStatus = "AVAILABLE" | "OCCUPIED" | "MAINTENANCE";

export interface Room {
  id: number;
  roomNumber: string;
  area: number; // in m2
  basePrice: number;
  capacity: number;
  currentTenants: number;
  status: RoomStatus;
  floor: number;
}
