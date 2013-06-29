import List

main = do
 xs <- readFile "FichasAlumnos2.csv"
 writeFile "alumnos2.txt" (procesa (lines xs))


contiene xs ys = any (xs `isPrefixOf`) (tails ys)

contieneUno xss ys = any (`contiene` ys) xss

procesa xs = 
  unlines .
  extrae . 
  map (tokens (==';')) .
  filter (contieneUno ["Apellido","Correo","Nombre","Teléfono"]) $ xs


extrae [] = []
extrae (x1:x2:x3:x4:x5:xs) = [ap1, ap2, nom, telef, movil, nacim, correo1, correo2] ++ extrae xs 
 where 
  ap1 = x1 !!! 1
  ap2 = x2 !!! 1
  nom = x3 !!! 1
  telef = x4 !!! 1
  movil = x4 !!! 3
  nacim = x2 !!! 3 
  correo1 = x5 !!! 1
  correo2 = x5 !!! 3


xs !!! p = if p < length xs then xs !! p else "Unknown" 



tokens sep xs
 | null ys = []
 | otherwise = zs : tokens sep vs 
 where
   ys = dropWhile (sep) xs
   (zs,vs) = span (not.sep) ys 