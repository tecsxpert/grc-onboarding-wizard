import ErrorBoundary from "./components/ErrorBoundary";
import ToolList from "./pages/ToolList";

function App() {
  return (
    <ErrorBoundary>
      <ToolList />
    </ErrorBoundary>
  );
}

export default App;