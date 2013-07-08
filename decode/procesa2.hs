-- En Alfil: Actas -> Seguimientos -> Incluir TODO -> Calificar -> Guardar como htm

import List hiding (find)
import System.Directory
import Char

file = "Alfil - Ver. 2.516.0.htm"

folder = "Alfil - Ver. 2.516.0_files"

main = do
 xs <- readFile file
 out (procesa xs)


quitaAcento 'Á' = 'A'
quitaAcento 'É' = 'E'
quitaAcento 'Í' = 'I'
quitaAcento 'Ó' = 'O'
quitaAcento 'Ú' = 'U'
quitaAcento x = x


normaliza = map quitaAcento . map toUpper


out xs = do
  createDirectory "out"
  createDirectory "out\\images"
  let rs = zip xs [0..]
  mapM createPhoto rs
  ys <- readFile "alumnos.txt"
  let zs = map (cruza (readFile2 . lines $ ys)) rs
    



  writeFile "out\\students.txt" (unlines . concat $ zs)



cruza f ((photo,dni,nombre,exp,matr,conv), id) = [ap1,ap2,nomb,telef,mob,date,mail1,mail2,newPhoto id, dni, exp, matr, conv]
 where
  (ap1,ap2,nomb,telef,mob,date,mail1,mail2) = find nombre f 
       

find nombre [] = error ("not found "++normaliza nombre)
find nombre (x@(ap1,ap2,nomb,telef,mob,date,mail1,mail2):xs)
 | contains (normaliza ap1) nombre' && contains (normaliza ap2) nombre' && contains (normaliza nomb) nombre' = x
 | otherwise = find nombre xs  
 where nombre' = normaliza nombre  


procesa3 [] = []
procesa3 (((photo,dni,nombre,exp,matr,conv), id):xs) =
 unlines [newPhoto id, dni, nombre, exp, matr, conv] : procesa3 xs


newPhoto id = "photo"++show id++".jpg"

createPhoto ((photo,dni,nombre,exp,matr,conv), id) =
 copyFile (folder++"\\"++photo)  ("out\\images\\"++newPhoto id)





procesa xs = 
    procesa2
  . dropWhile (not . isPhoto)
  . filter isHtml
  . items
  $ xs





contains xs ys = any (isPrefixOf xs) (tails ys)


data Item = Html String | Value String deriving Show

items [] = []
items (x:xs)
 | x == '<'   = Html ys : items (tail zs)
 | otherwise  = Value (x:ys) : items zs' 
 where
  (ys,zs) = span (/='>') xs
  (ys',zs') = span (/='<') xs
 
isHtml (Html _) = True
isHtml _        = False

isImg (Html xs) = isPrefixOf "img" xs
isImg _         = False

isPhoto html@(Html xs) = isImg html && contains "fotografia" xs



procesa2 []      = []
procesa2 (photo:_:_:dni:_:_:nombre:_:_:exp:_:_:matr:_:_:conv:_:_:nota:_:_:calif:_:_:matric:xs) 
 | isPhoto photo = (photoOf photo, dniOf dni, nameOf nombre, expOf exp, matrOf matr, convOf conv)  : procesa2 (drop 14 xs)
 | otherwise     = []
 


valueOf = 
   takeWhile (/= '\"')
 . drop (length "value=\"")
 . dropPWhile (isPrefixOf "value") 

nameOf (Html xs) = valueOf xs


photoOf (Html xs) = 
   takeWhile (/= '\"')
 . drop (length "_files/")
 . dropPWhile (isPrefixOf "_files") 
 $ xs

dniOf (Html xs) =  valueOf xs

expOf (Html xs) = valueOf xs

matrOf (Html xs) = valueOf xs

convOf (Html xs) = valueOf xs

dropPWhile p [] = []
dropPWhile p (x:xs) 
  | p (x:xs) = x:xs
  | otherwise = dropPWhile p xs 







readFile2 [] = []
readFile2 (ap1:ap2:nomb:telef:mob:date:mail1:mail2:xs) = 
 (ap1,ap2,nomb,telef,mob,date,mail1,mail2) : readFile2 xs
