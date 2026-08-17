import sys
import os
from PIL import Image

def crop_jei(input_path, output_path):
    img = Image.open(input_path).convert('RGB')
    w, h = img.size
    cx, cy = w // 2, h // 2
    
    crop_w = 383
    crop_h = 208
    
    left = cx - crop_w // 2
    top = cy - (crop_h // 2) - 9
    right = left + crop_w
    bottom = cy + (crop_h // 2)
    
    cropped = img.crop((left, top, right, bottom))
    cropped.save(output_path)
    print(f"Cropped {input_path} to {output_path}")

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python crop_jei.py <input> <output>")
        sys.exit(1)
    crop_jei(sys.argv[1], sys.argv[2])
