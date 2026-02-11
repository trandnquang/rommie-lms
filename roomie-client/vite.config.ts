import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true, // Listen on all network interfaces (needed for Docker)
    port: 5173, // Default Vite port
    strictPort: true, // Fail if port is already in use
  },
  preview: {
    port: 80, // Map to Nginx default port when previewing build
  },
});
