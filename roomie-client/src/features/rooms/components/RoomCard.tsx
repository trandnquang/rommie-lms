import React from "react";
import { Room } from "../types";
import { formatCurrency } from "../../../utils/format";
import { Users, Maximize } from "lucide-react";

interface RoomCardProps {
  room: Room;
}

const statusColors = {
  AVAILABLE: "bg-green-100 text-green-700 border-green-200",
  OCCUPIED: "bg-indigo-100 text-indigo-700 border-indigo-200",
  MAINTENANCE: "bg-gray-100 text-gray-600 border-gray-200",
};

export const RoomCard: React.FC<RoomCardProps> = ({ room }) => {
  return (
    <div className="bg-white rounded-xl border border-gray-200 shadow-sm hover:shadow-md transition-all duration-200 overflow-hidden">
      {/* Card Header: Room Number & Status */}
      <div className="p-5 border-b border-gray-100 flex justify-between items-start">
        <div>
          <span className="text-xs font-semibold text-gray-400 uppercase tracking-wider">
            Room
          </span>
          <h3 className="text-2xl font-bold text-gray-800">
            {room.roomNumber}
          </h3>
        </div>
        <span
          className={`px-3 py-1 rounded-full text-xs font-bold border ${statusColors[room.status]}`}
        >
          {room.status}
        </span>
      </div>

      {/* Card Body: Info */}
      <div className="p-5 space-y-4">
        <div className="flex justify-between items-center text-sm">
          <div className="flex items-center text-gray-500">
            <Maximize size={16} className="mr-2" />
            <span>{room.area} m²</span>
          </div>
          <div className="flex items-center text-gray-500">
            <Users size={16} className="mr-2" />
            <span>
              {room.currentTenants}/{room.capacity}
            </span>
          </div>
        </div>

        <div className="pt-2">
          <p className="text-xs text-gray-400 mb-1">Monthly Rent</p>
          <p className="text-lg font-bold text-indigo-600">
            {formatCurrency(room.basePrice)}
          </p>
        </div>
      </div>

      {/* Card Footer: Action */}
      <div className="px-5 py-3 bg-gray-50 border-t border-gray-100">
        <button className="w-full text-sm font-medium text-gray-600 hover:text-indigo-600 transition-colors">
          View Details
        </button>
      </div>
    </div>
  );
};
