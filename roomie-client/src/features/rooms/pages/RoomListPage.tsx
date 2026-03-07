import React from "react";
import { DashboardLayout } from "../../../layouts/DashboardLayout";
import { RoomCard } from "../components/RoomCard";
import { MOCK_ROOMS } from "../data/mockRooms";
import { Plus, Search, Filter } from "lucide-react";

export const RoomListPage: React.FC = () => {
  return (
    <DashboardLayout>
      {/* Page Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between mb-8 gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Room Management</h1>
          <p className="text-gray-500 mt-1">
            Overview of all properties and occupancy
          </p>
        </div>

        <button className="flex items-center justify-center px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors shadow-sm">
          <Plus size={20} className="mr-2" />
          Add New Room
        </button>
      </div>

      {/* Filters & Search Bar */}
      <div className="bg-white p-4 rounded-xl border border-gray-200 shadow-sm mb-6 flex flex-col md:flex-row gap-4">
        <div className="relative flex-1">
          <Search
            className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
            size={20}
          />
          <input
            type="text"
            placeholder="Search room number..."
            className="w-full pl-10 pr-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
          />
        </div>

        <button className="flex items-center px-4 py-2 border border-gray-200 rounded-lg text-gray-600 hover:bg-gray-50">
          <Filter size={18} className="mr-2" />
          Status: All
        </button>
      </div>

      {/* Room Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        {MOCK_ROOMS.map((room) => (
          <RoomCard key={room.id} room={room} />
        ))}
      </div>
    </DashboardLayout>
  );
};
