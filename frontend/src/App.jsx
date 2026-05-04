import ErrorBoundary from "./components/ErrorBoundary";
import Navbar from "./components/Navbar";
import ToolList from "./pages/ToolList";

export default function App() {
  return (
    <ErrorBoundary>
      <Navbar />
      <ToolList />
    </ErrorBoundary>
  );
}