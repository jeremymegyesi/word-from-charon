function Spinner() {
    return (
    <div className="flex justify-center items-center h-32 text-primary">
      <div className="flex space-x-2">
        <span className="dot animate-dot delay-0">.</span>
        <span className="dot animate-dot delay-150">.</span>
        <span className="dot animate-dot delay-300">.</span>
      </div>

      <style>
        {`
          .dot {
            font-size: 2rem;
            font-weight: bold;
            opacity: 0.5;
          }
          @keyframes dotPulse {
            0% { transform: scale(1); opacity: 0.5; }
            50% { transform: scale(1.5); opacity: 1; }
            100% { transform: scale(1); opacity: 0.5; }
          }
          .animate-dot {
            animation: dotPulse 1s infinite ease-in-out;
          }
          .delay-0 { animation-delay: 0s; }
          .delay-150 { animation-delay: 0.15s; }
          .delay-300 { animation-delay: 0.3s; }
        `}
      </style>
    </div>

    );
}

export { Spinner };
