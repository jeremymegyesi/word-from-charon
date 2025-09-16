import { useNavigate } from 'react-router-dom'

export function Header() {
  const navigate = useNavigate()

  const handleLogoClick = () => {
    navigate('/')
  }

  return (
    <header className="bg-primary text-primary-foreground border-b border-primary/20">
      <div className="container mx-auto px-4 py-3 max-w-6xl">
        <div 
          className="flex items-center gap-3 cursor-pointer hover:opacity-80 transition-opacity w-fit"
          onClick={handleLogoClick}
        >
          {/* Sample Logo */}
          <div className="flex items-center justify-center w-13 h-13">
            <img src="src/assets/charon.svg" />
          </div>
          
          {/* App Name */}
          <div>
            <h1 className="text-2xl font-medium">Word from Charon</h1>
            <p className="text-s text-primary-foreground/70">Real-time transit insights</p>
          </div>
        </div>
      </div>
    </header>
  )
}