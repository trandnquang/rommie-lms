import json
from pathlib import Path

def standardize_address_data(input_file, output_file):
    try:
        # 1. Đọc file tree.json
        print(f"--- Đang đọc file: {input_file} ---")
        with open(input_file, 'r', encoding='utf-8') as f:
            raw_data = json.load(f)

        standardized_data = []

        # 2. Xử lý cấp Tỉnh (Provinces)
        # Dữ liệu gốc là Dictionary {"code": {...}}, cần lấy .values()
        # Sắp xếp theo Code (ép kiểu int) để danh sách không bị lộn xộn
        provinces = sorted(raw_data.values(), key=lambda x: int(x['code']))

        for p in provinces:
            province_node = {
                "name": p.get("name_with_type"), # Lấy tên đầy đủ: "Thành phố Hồ Chí Minh"
                "code": p.get("code"),
                "unit_type": p.get("type"),      # Ví dụ: "thanh-pho"
                "districts": []
            }

            # 3. Xử lý cấp Quận/Huyện
            # Truy cập vào key "quan-huyen"
            raw_districts = p.get("quan-huyen", {})
            if raw_districts:
                districts = sorted(raw_districts.values(), key=lambda x: int(x['code']))
                
                for d in districts:
                    district_node = {
                        "name": d.get("name_with_type"), # "Quận 1"
                        "code": d.get("code"),
                        "unit_type": d.get("type"),      # Ví dụ: "quan"
                        "wards": []
                    }

                    # 4. Xử lý cấp Phường/Xã
                    # Truy cập vào key "xa-phuong"
                    raw_wards = d.get("xa-phuong", {})
                    if raw_wards:
                        wards = sorted(raw_wards.values(), key=lambda x: int(x['code']))
                        
                        for w in wards:
                            ward_node = {
                                "name": w.get("name_with_type"), # "Phường Bến Nghé"
                                "code": w.get("code"),
                                "unit_type": w.get("type")       # Ví dụ: "phuong"
                            }
                            district_node["wards"].append(ward_node)
                    
                    province_node["districts"].append(district_node)
            
            standardized_data.append(province_node)

        # 5. Xuất ra file JSON chuẩn
        # Tạo thư mục output nếu chưa có
        Path(output_file).parent.mkdir(parents=True, exist_ok=True)
        
        with open(output_file, 'w', encoding='utf-8') as f:
            json.dump(standardized_data, f, ensure_ascii=False, indent=2)
        
        print(f"Thành công! Đã xử lý {len(standardized_data)} tỉnh thành.")
        print(f"File kết quả: {output_file}")

    except Exception as e:
        print(f"Lỗi: {e}")

if __name__ == "__main__":
    # Đảm bảo file tree.json nằm cùng cấp hoặc chỉnh lại đường dẫn
    input_path = "tree.json" 
    output_path = "output/address_master.json"
    standardize_address_data(input_path, output_path)