import React from "react";
import { Home, Users, FileText, Settings, LogOut } from "lucide-react"; // Using lucide-react or MUI Icons

interface DashboardLayoutProps {
  children: React.ReactNode;
}

const SidebarItem = ({
  icon: Icon,
  label,
  active,
}: {
  icon: React.ElementType;
  label: string;
  active?: boolean;
}) => (
  <div
    className={`flex items-center space-x-3 p-3 rounded-lg cursor-pointer transition-colors ${
      active ? "bg-indigo-50 text-indigo-600" : "text-gray-600 hover:bg-gray-50"
    }`}
  >
    <Icon size={20} />
    <span className="font-medium">{label}</span>
  </div>
);

export const DashboardLayout: React.FC<DashboardLayoutProps> = ({
  children,
}) => {
  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* Sidebar */}
      <aside className="w-64 bg-white border-r border-gray-200 fixed h-full z-10 hidden md:block">
        <div className="p-6 border-b border-gray-100">
          <h1 className="text-2xl font-bold text-indigo-600">
            Roomie<span className="text-gray-400">.</span>
          </h1>
        </div>

        <nav className="p-4 space-y-2">
          <SidebarItem icon={Home} label="Rooms" active />
          <SidebarItem icon={Users} label="Tenants" />
          <SidebarItem icon={FileText} label="Contracts" />
          <SidebarItem icon={FileText} label="Invoices" />

          <div className="pt-8 mt-8 border-t border-gray-100">
            <SidebarItem icon={Settings} label="Settings" />
            <SidebarItem icon={LogOut} label="Logout" />
          </div>
        </nav>
      </aside>

      {/* Main Content */}
      <main className="flex-1 md:ml-64 p-8">{children}</main>
    </div>
  );
};
