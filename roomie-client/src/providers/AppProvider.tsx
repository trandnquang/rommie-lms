import React from "react";
import { QueryClientProvider } from "@tanstack/react-query";
import { queryClient } from "@/lib/react-query";
import { BrowserRouter } from "react-router-dom";

interface AppProviderProps {
    children: React.ReactNode;
}

export const AppProvider: React.FC<AppProviderProps> = ({ children }) => {
    return (
        <QueryClientProvider client={queryClient}>
            <BrowserRouter>{children}</BrowserRouter>
        </QueryClientProvider>
    );
};
