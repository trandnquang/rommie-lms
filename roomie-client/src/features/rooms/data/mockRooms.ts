import { Room } from "../types";

export const MOCK_ROOMS: Room[] = [
  {
    id: 1,
    roomNumber: "101",
    area: 25,
    basePrice: 2500000,
    capacity: 2,
    currentTenants: 2,
    status: "OCCUPIED",
    floor: 1,
  },
  {
    id: 2,
    roomNumber: "102",
    area: 30,
    basePrice: 3000000,
    capacity: 3,
    currentTenants: 0,
    status: "AVAILABLE",
    floor: 1,
  },
  {
    id: 3,
    roomNumber: "201",
    area: 28,
    basePrice: 2800000,
    capacity: 2,
    currentTenants: 0,
    status: "MAINTENANCE",
    floor: 2,
  },
  {
    id: 4,
    roomNumber: "202",
    area: 35,
    basePrice: 3500000,
    capacity: 4,
    currentTenants: 3,
    status: "OCCUPIED",
    floor: 2,
  },
];
